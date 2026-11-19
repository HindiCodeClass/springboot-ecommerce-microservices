package com.ecommerce.order.saga;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "saga_state", indexes = {
		@Index(name = "idx_saga_state_order_number", columnList = "orderNumber"),
		@Index(name = "idx_saga_state_status", columnList = "status")
})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SagaState {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
	@Column(nullable = false, unique = true, updatable = false, length = 100)
	private String sagaId;
	@Column(nullable = false, unique = true, length = 100)
	private String orderNumber;
	@Column(nullable = false, length = 100)
	private String skuCode;
	@Column(nullable = false)
	private Integer quantity;
	@Enumerated(EnumType.STRING)
	private SagaStatus status;
	@Enumerated(EnumType.STRING)
	private SagaStep currentStep;
	private Long paymentId;
	private Integer retryCount;
	private String lastError;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime completedAt;
	@Version
	private Long version;
	@PrePersist
	protected void onCreate() {
		if(sagaId == null) {
			sagaId = UUID.randomUUID().toString();
			
		}
		if(retryCount == null){
			retryCount = 0;
		}
		createdAt = LocalDateTime.now();
		updatedAt= createdAt;
	}
	@PreUpdate
	protected void onupdate() {
		updatedAt = LocalDateTime.now();
	}
}
