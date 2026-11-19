package com.ecommerce.inventory.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventory_reservation", uniqueConstraints = {
		@UniqueConstraint(name = "uk_reservation_order", columnNames = "orderId")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryReservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String orderId;
	private String skuCode;
	private Integer quantity;
	@Enumerated(EnumType.STRING)
	private ReservationStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	@Version
	private Long version;

}
