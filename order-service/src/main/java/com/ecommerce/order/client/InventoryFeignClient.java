package com.ecommerce.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.order.dto.InventoryResponse;

//@FeignClient(name = "inventory-service")
public interface InventoryFeignClient {
		@GetMapping("/api/inventory/{skuCode}")
		InventoryResponse isInStock(@PathVariable String skuCode);
}
