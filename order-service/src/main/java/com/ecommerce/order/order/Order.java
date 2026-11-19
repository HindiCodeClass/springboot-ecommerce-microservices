package com.ecommerce.order.order;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders", indexes = {
		@Index(name = "idx_order_number", columnList = "order_number"),
		@Index( name ="idx_order_status", columnList = "order_status"),
		@Index( name ="idx_order_status_sku", columnList = "order_status, sku_code")
		
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
	@Column( name = "order_number", nullable = false)
		private String orderNumber;
	@Column(name = "sku_code")
		private String skuCode;
		private Integer quantity;
		private BigDecimal price;
		@Column(name = "order_status")
		private String orderStatus;
}
