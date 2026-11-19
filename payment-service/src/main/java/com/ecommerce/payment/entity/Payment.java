package com.ecommerce.payment.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ecommerce.payment.enums.PaymentMethod;
import com.ecommerce.payment.enums.PaymentStatus;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments", indexes = {
		@Index(columnList = "orderId"),
		@Index(columnList = "transactionId")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
	@Column(nullable = false)
		private String orderId;
	@Column(nullable = false, precision = 12, scale = 2)
		private BigDecimal amount;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
		private PaymentStatus paymentStatus;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
		private PaymentMethod paymentMethod;
	@Column(unique = true, nullable = false)
		private String transactionId;
		private String gatewayPaymentId;
		private String gatewayOrderId;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
		private String sagaId;
		
		@PrePersist
		void onCreate() {
			this.createdAt = LocalDateTime.now();
			this.updatedAt = LocalDateTime.now();
		}
		@PreUpdate
		void onUpdate() {
			this.updatedAt = LocalDateTime.now();
		}
}
