package com.ecommerce.payment.service;

import org.springframework.stereotype.Service;

import com.ecommerce.payment.DTO.CreatePaymentRequest;
import com.ecommerce.payment.DTO.GatewayOrderResponse;
import com.ecommerce.payment.DTO.PaymentResponse;
import com.ecommerce.payment.DTO.PaymentStatusUpdateRequest;
import com.ecommerce.payment.DTO.RequestPayment;
import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.enums.PaymentMethod;
import com.ecommerce.payment.enums.PaymentStatus;
import com.ecommerce.payment.exception.PaymentNotFoundException;
import com.ecommerce.payment.repository.PaymentRepository;
import com.ecommerce.payment.util.TransactionGenerator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl  implements PaymentService{
	private final PaymentRepository paymentRepo;
	private final RozarpayPaymentGateway paymentGateway;

	@Override
	public PaymentResponse createPayment(RequestPayment request) {
		 Payment payment =  Payment.builder()
		.orderId(request.getOrderId())
		.amount(request.getAmount())
		.paymentStatus(PaymentStatus.PENDING)
		.transactionId(TransactionGenerator.generate()).build();
		 Payment savedPayment = paymentRepo.save(payment);
		return mapToResponse(savedPayment);
	}

	@Override
	public PaymentResponse getPaymentByOrderId(String orderId) {
		Payment payment = paymentRepo.findByOrderId(orderId).orElseThrow(()-> new PaymentNotFoundException("Payment not found for this orderid "+orderId));
		return mapToResponse(payment);
	}

	@Override
	public PaymentResponse updatePaymentStatus(Long PaymentId, PaymentStatusUpdateRequest request) {
		// TODO Auto-generated method stub
		Payment payment = paymentRepo.findById(PaymentId).orElseThrow(()-> new PaymentNotFoundException("payment not found with id "+PaymentId));
		payment.setPaymentStatus(request.getPaymentStatus());
		return mapToResponse(payment);
	}
	private PaymentResponse mapToResponse(Payment payment) {
		return new PaymentResponse(payment.getId(),
				payment.getOrderId(),
				payment.getAmount(),
				payment.getPaymentStatus(),
				payment.getTransactionId(),
				 payment.getGatewayOrderId());
	}
	public PaymentResponse createSagaPayment(CreatePaymentRequest request) {
		 PaymentMethod paymentMethod = PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase());
		Payment payment = Payment.builder()
				.sagaId(request.getSagaId())
				.orderId(request.getOrderId())
				.amount(request.getAmount())
				.paymentMethod(paymentMethod)
				.paymentStatus(PaymentStatus.PENDING)
				.transactionId(TransactionGenerator.generate()).build();
		Payment savedPayment = paymentRepo.save(payment);
		try {
	        GatewayOrderResponse gatewayResponse =paymentGateway.createOrder(savedPayment.getOrderId(), savedPayment.getAmount());
	        savedPayment.setGatewayOrderId(gatewayResponse.getGatewayOrderId());
	        Payment updatedPayment = paymentRepo.save(savedPayment);
	        return mapToResponse(updatedPayment);

	    } catch (RuntimeException ex) {
	        savedPayment.setPaymentStatus(PaymentStatus.FAILED);
	        paymentRepo.save(savedPayment);
	        throw ex;
	    }
	}
}
