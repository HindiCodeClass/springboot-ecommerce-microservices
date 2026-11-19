package com.ecommerce.product.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductEventPublisher {
private static final String TOPIC = "product-cache-evet";
private final KafkaTemplate<String, String> kafkaTemplate;
private final ObjectMapper objectMapper = new ObjectMapper();
public void publishProductCreated(Long productId) {
	
	try{ 
		ProductCacheEvent event = new ProductCacheEvent("PRODUCT_CREATED", productId);
	
	String jsonEvent = objectMapper.writeValueAsString(event);
	kafkaTemplate.send(TOPIC, productId.toString(),jsonEvent);
	}catch (Exception e) {
		// TODO: handle exception
	}
}
public void publishProductUpdated(Long productId) {
	try {
	ProductCacheEvent event = new ProductCacheEvent("PRODUCT_UPDATED", productId);
	String jsonEvent = objectMapper.writeValueAsString(event);
	kafkaTemplate.send(TOPIC, productId.toString(),jsonEvent);
}catch (Exception e) {
	// TODO: handle exception
}
}
public void publishProductDeleted(Long productId) {
	try {
	ProductCacheEvent event = new ProductCacheEvent("PRODUCT_DELETED", productId);
	String jsonEvent = objectMapper.writeValueAsString(event);
	kafkaTemplate.send(TOPIC, productId.toString(),jsonEvent);
	}catch (Exception e) {
		// TODO: handle exception
	}
}
}
