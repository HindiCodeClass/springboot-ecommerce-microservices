package com.ecommerce.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
		private Long id;
		@NotBlank(message = "Product name is required")
		private String name;
		@NotBlank
		private String description;
		@Positive(message = "price must be positive")
		private double price;
		private double discountPrice;
		@Min(value = 0, message = "Quantity must be 0 or more")
		private int quantity;
		private String brand;
		private String imageUrl;
		@NotNull(message = "Category id is required")
		private Long categoryId;
		private String categoryName;
}
