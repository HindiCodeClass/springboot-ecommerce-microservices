package com.ecommerce.order.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderRequest {
	private String skuCode;
	@Min(value = 1, message = "quantity must be at least 1")
	private Integer quantity;
	@NotNull(message = "price is mandatory")
	private BigDecimal price;
	@NotBlank
private String paymentMethod;
}

