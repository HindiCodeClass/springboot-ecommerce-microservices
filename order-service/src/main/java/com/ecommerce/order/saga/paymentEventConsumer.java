package com.ecommerce.order.saga;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ecommerce.order.sagPaymentEventa.PaymentEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class paymentEventConsumer {
	private final ObjectMapper objectMapper;
private final orderSagaOrchestrator sagaOrchestrator;
@KafkaListener(topics = "payment-event", groupId = "order-saga-group")
public void consume(String  request) {
	try {
		PaymentEvent event = objectMapper.readValue(request, PaymentEvent.class);
		switch (event.getEventType()) {
		case "PAYMENT_SUCCESS" -> sagaOrchestrator.handlePaymentSuccess(event);
		case "PAYMENT_FAILED" -> sagaOrchestrator.handlePaymentFailure(event);
		default -> log.warn("unknown payment event:{}", event.getEventType());
		}
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	
}
}
