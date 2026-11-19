package com.ecommerce.order.order;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.Collate;
import org.hibernate.boot.model.internal.Nullability;

import com.ecommerce.order.client.OutboxStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "outbox_Events", indexes = {
		@Index(name= "idx_outbox_status_created", columnList = "status, createdAt")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	private String eventId;
	private String aggregateId;
	private String aggregateType;
	private String eventType;
	@Lob
	private String payload;
	@Enumerated(EnumType.STRING)
	private OutboxStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime publishedAt;
}
