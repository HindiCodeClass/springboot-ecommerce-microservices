package com.ecommerce.inventory.entity;

import org.hibernate.annotations.Collate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory", indexes = {
		@Index(name = "idx_inventory_sku",
				columnList = "sku_code")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
	@Column(unique = true, nullable = false, name = "sku_code")
		private String skuCode;
	private Integer availableQuantity;
	private Integer reserveQuantity;
	@Version
	private Long version;
}
