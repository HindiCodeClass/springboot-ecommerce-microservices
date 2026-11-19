package com.ecommerce.payment.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.ecommerce.payment.DTO.PaymentEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {
private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;
public void publish(PaymentEvent event) {
	kafkaTemplate.send("payment-event", event.getOrderId(), event).whenComplete((result, ex)->{
		if(ex !=null) {log.error("failed to publish payment event. ordedrId{}", event.getOrderId(), ex);
		return;
		}
		log.info("paymentEvent published. eventType{}, orderId{}", event.getEventType(), event.getOrderId());
	});
}
}
