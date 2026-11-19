package com.ecommerce.order.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ecommerce.order.event.OrderPlacedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {
private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

public void sendOrderEvent(OrderPlacedEvent event) {
	kafkaTemplate.send("order-event", event.getOrderId().toString(), event)
	.whenComplete((result, ex) -> {
		if(ex != null) {
			log.error("failed to publish order event. orderId={}", event.getOrderId(),ex);
			return;
		}
		log.info("Order event published successfully. orderId={}, partition={}, offser={}", event.getOrderId(),result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
	});
	
}
}
