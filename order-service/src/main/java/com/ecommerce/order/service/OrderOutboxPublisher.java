package com.ecommerce.order.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ecommerce.order.client.OutboxStatus;
import com.ecommerce.order.event.OrderPlacedEvent;
import com.ecommerce.order.order.OutboxEvent;
import com.ecommerce.order.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderOutboxPublisher {
private final OutboxEventRepository outboxRepo;
private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
private final ObjectMapper objectMapper;
@Scheduled(fixedDelay = 1000)
@Transactional
public void publishEvent() {
	System.out.println("outbox publishing event");
	List<OutboxEvent> events = outboxRepo.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus.NEW);
	for(OutboxEvent event: events) {
	try {
		OrderPlacedEvent outbox = objectMapper.readValue(event.getPayload(), OrderPlacedEvent.class);
		kafkaTemplate.send("order-event", outbox.getOrderId(), outbox).get();
		event.setStatus(OutboxStatus.PUBLISHED);
		event.setPublishedAt(LocalDateTime.now());
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
}
