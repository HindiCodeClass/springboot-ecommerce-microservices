package com.ecommerce.payment.DTO;

import java.math.BigDecimal;

import com.ecommerce.payment.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
		private Long PaymentId;
		private String orderId;
		private BigDecimal amount;
		private PaymentStatus paymentStatus;
		private String transactionId;
	    private String gatewayOrderId;
}
