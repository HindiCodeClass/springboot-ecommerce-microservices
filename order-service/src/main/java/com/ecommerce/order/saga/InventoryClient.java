package com.ecommerce.order.saga;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service")
public interface InventoryClient {
	@PostMapping("/api/inventory/{orderId}/reserve")
void reserve(@PathVariable String orderId,
		@RequestParam String skuCode,
		@RequestParam Integer quantity);
@PostMapping("/api/inventory/{orderId}/commit")
void commit(@PathVariable String orderId);
@PostMapping("/api/inventory/{orderId}/release")
void release(@PathVariable String orderId);
}
