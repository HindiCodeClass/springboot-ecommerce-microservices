package com.ecommerce.order.saga;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePaymentRequest {
private String sagaId;
private String orderId;
private BigDecimal amount;
private String paymentMethod;
}
