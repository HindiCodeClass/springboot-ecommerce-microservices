package com.ecommerce.inventory.Mapper;

import org.springframework.stereotype.Component;

import com.ecommerce.inventory.dto.InventoryRequest;
import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.entity.Inventory;

@Component
public class InventoryMapper {
		public Inventory toEntity(InventoryRequest request) {
		Inventory inventory = new Inventory();
		inventory.setSkuCode(request.getSkuCode());
		inventory.setAvailableQuantity(request.getQuantity());
		return inventory;
		}
		public InventoryResponse toresponse(Inventory inventory) {
			return new InventoryResponse(
					inventory.getSkuCode(),
					inventory.getAvailableQuantity()>0,
					inventory.getAvailableQuantity());
		}
}
