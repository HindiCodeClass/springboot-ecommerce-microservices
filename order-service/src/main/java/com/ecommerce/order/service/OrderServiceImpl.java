package com.ecommerce.order.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.dto.PlaceOrderRequest;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.order.Order;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.saga.PaymentResponse;
import com.ecommerce.order.saga.orderSagaOrchestrator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

		private final OrderRepository orderRepo;
		private final OrderMapper orderMapper;
	//	private final OrderEventProducer orderEventProducer;
	//	private final InventoryFeignClient inventoryFeignClient;
	//	private final ObjectMapper objectMapper;
	//	private final OutboxEventRepository outboxRepo;
		private final orderSagaOrchestrator sagaOrchestrator;

   
		@Override
		@Transactional
		public OrderResponse PlaceOrder(PlaceOrderRequest request) {
			
		String paymentMethod = request.getPaymentMethod();
		Order order = orderMapper.toEntity(request);
		String orderID = UUID.randomUUID().toString();
		order.setOrderNumber(orderID);
		order.setOrderStatus("SAGA_STARTED");
		Order saved = orderRepo.save(order);
				PaymentResponse payment =  sagaOrchestrator.startSaga(saved, paymentMethod);
				return new OrderResponse(saved.getOrderNumber(), saved.getOrderStatus());
		/*InventoryResponse inventory = inventoryFeignClient.isInStock(request.getSkuCode());
			if(!inventory.isInStock()) {
				throw new RuntimeException("product is out of stock");
			}
			Order order = orderMapper.toEntity(request);
			String orderId = UUID.randomUUID().toString();
			order.setOrderNumber(orderId);
			order.setOrderStatus("CREATED");
			Order savedOrder =  orderRepo.save(order);
			OrderPlacedEvent event = new OrderPlacedEvent();
			event.setEventId(Uuid.randomUuid().toString());
			event.setOrderId(orderId);
			event.setSkuCode(request.getSkuCode());
			event.setQuantity(request.getQuantity());
			
			event.setEventTime(LocalDateTime.now());
			String payload;
			try {
				payload = objectMapper.writeValueAsString(event);
			} catch (Exception e) {
				throw new IllegalStateException("Failed to Order Event");
			}
		OutboxEvent outboxEvent  =OutboxEvent.builder()
				.eventId(event.getEventId())
				.aggregateId(orderId)
				.aggregateType("ORDER")
				.eventType("ORDER_PLACED")
				.payload(payload)
				.status(OutboxStatus.NEW)
				.createdAt(LocalDateTime.now()).build();
		outboxRepo.save(outboxEvent);
			return orderMapper.toResponse(savedOrder);
		}*/
		}
}
