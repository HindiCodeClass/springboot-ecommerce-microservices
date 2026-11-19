package com.ecommerce.order.saga;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.order.order.Order;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.sagPaymentEventa.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class orderSagaOrchestrator {

   // private final OrderOutboxPublisher orderOutboxPublisher;

private final OrderRepository orderRepo;
private final OrderSagaRepository sagaRepo;
private final InventoryClient inventoryClient;

private final PaymentClient paymentClient;
 
@Transactional
public PaymentResponse startSaga(Order order, String paymentMethod) {
	SagaState saga = SagaState.builder()
			.orderNumber(order.getOrderNumber())
			.skuCode(order.getSkuCode())
			.quantity(order.getQuantity())
			.status(SagaStatus.sAGA_STARTED)
			.currentStep(SagaStep.CREATE_ORDER)
			.retryCount(0).build();
	sagaRepo.save(saga);
	try {
		reserveInventory(saga);
		PaymentResponse payment =  createPayment(saga, order, paymentMethod);
		saga.setStatus(SagaStatus.PAYMENT_PENDING);
		saga.setCurrentStep(SagaStep.WAIT_FOR_PAYMENT);
		sagaRepo.save(saga);
		return payment;
	}catch (Exception ex) {
		log.error("Saga failed. sagaId{}, orderId{}", saga.getSagaId(), saga.getOrderNumber(),ex);
	compensate(saga);
	throw ex;
	}
}
private void reserveInventory(SagaState saga) {
	saga.setStatus(SagaStatus.INVENTORY_RESERVING);
	saga.setCurrentStep(SagaStep.RESERVE_INVENTORY);
	sagaRepo.save(saga);
	inventoryClient.reserve(saga.getOrderNumber(), saga.getSkuCode(), saga.getQuantity());
	saga.setStatus(SagaStatus.INVENTORY_RESERVED);
	sagaRepo.save(saga);
}
private PaymentResponse createPayment(SagaState saga, Order order, String paymentMethod) {
	saga.setCurrentStep(SagaStep.CREATE_PAYMENT);
	sagaRepo.save(saga);
	CreatePaymentRequest request = CreatePaymentRequest.builder()
			.sagaId(saga.getSagaId())
			.orderId(order.getOrderNumber())
			.amount(order.getPrice())
			.paymentMethod(paymentMethod).build();
	return paymentClient.createSagaPayment(request);
}
@Transactional
public void handlePaymentSuccess(PaymentEvent event) {
	SagaState saga  =sagaRepo.findBySagaId(event.getSagaId()).orElseThrow(()-> new IllegalStateException("saga not found"));
	if(saga.getStatus() == SagaStatus.COMPLETED) {return;

	}
	saga.setStatus(SagaStatus.PAYMENT_SUCCESS);
	saga.setCurrentStep(SagaStep.COMMIT_INVENTORY);
	saga.setPaymentId(event.getPaymentId());
	sagaRepo.save(saga);
	try {
		inventoryClient.commit(saga.getOrderNumber());
		saga.setStatus(SagaStatus.INVENTORY_COMMITTED);
		sagaRepo.save(saga);
		
	}catch (Exception e) {
		log.error("inventory commit failed. sagaId = {}", saga.getSagaId(), e);
		compensateAfterPayment(saga, event.getEventId());
		return;
	}
	
		Order order = orderRepo.findByOrderNumber(saga.getOrderNumber()).orElseThrow();
		order.setOrderStatus("CONFIRMED");
		orderRepo.save(order);
		saga.setStatus(SagaStatus.COMPLETED);
		saga.setCompletedAt(LocalDateTime.now());
		saga.setCurrentStep(SagaStep.CONFIRM_ORDER);
		sagaRepo.save(saga);
	
}
private void compensate(SagaState saga) {
	saga.setStatus(SagaStatus.COMPENSATING);
	sagaRepo.save(saga);
	try {
		inventoryClient.release(saga.getOrderNumber());
	} catch (Exception e) {
		saga.setLastError(e.getMessage());
		sagaRepo.save(saga);
		log.error("Inventory compensation failed. sagaId={}", saga.getSagaId(),e);
	}
}
private void compensateAfterPayment(SagaState saga , String paymentId) {
	saga.setStatus(SagaStatus.COMPENSATING);
	saga.setCurrentStep(SagaStep.REFUND_PAYMENT);
	sagaRepo.save(saga);
	try {
		paymentClient.refund(Long.valueOf(paymentId));
	} catch (Exception e) {
	saga.setLastError(e.getMessage());
	saga.setRetryCount(saga.getRetryCount()+1);
	sagaRepo.save(saga);
	log.error("payment Refund failed. sagaId = {}", saga.getSagaId(), e);
	return;
	}
	Order order = orderRepo.findByOrderNumber(saga.getOrderNumber()).orElseThrow();
	order.setOrderStatus("CANCELLED");
	orderRepo.save(order);
	saga.setStatus(SagaStatus.FAILED);
	sagaRepo.save(saga);
}
public void handlePaymentFailure(PaymentEvent event) { 
	SagaState saga  =sagaRepo.findBySagaId(event.getSagaId()).orElseThrow(()-> new IllegalStateException("saga not found"));

    if (saga.getStatus() == SagaStatus.COMPLETED 
            || saga.getStatus() == SagaStatus.FAILED) { 
        return; 
    } 

    if (saga.getStatus() == SagaStatus.PAYMENT_SUCCESS 
            || saga.getStatus() == SagaStatus.INVENTORY_COMMITTED) { 
        log.warn( 
            "Ignoring late payment failure for saga {}", 
            saga.getSagaId() 
        ); 
        return; 
    } 

    saga.setStatus(SagaStatus.COMPENSATING); 
    saga.setCurrentStep(SagaStep.RELEASE_INVENTORY); 
    sagaRepo.save(saga); 

    try { 
        inventoryClient.release(saga.getOrderNumber()); 
    } catch (Exception ex) { 
    	saga.setLastError(ex.getMessage());
    	saga.setRetryCount(saga.getRetryCount()+1);
    	sagaRepo.save(saga);
    } 

    Order order = orderRepo.findByOrderNumber(saga.getOrderNumber()).orElseThrow();
	order.setOrderStatus("CANCELLED");
	orderRepo.save(order);
	saga.setStatus(SagaStatus.FAILED);
	sagaRepo.save(saga);
} 

}
