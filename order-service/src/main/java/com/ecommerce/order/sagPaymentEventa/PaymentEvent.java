package com.ecommerce.order.sagPaymentEventa;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PaymentEvent {
private String eventId;
private String sagaId;
private String orderId;
private Long paymentId;
private String gatewayPaymentId;
private String gatewayOrderId;
private BigDecimal amount;
private String eventType;
private LocalDateTime eventTime;
}
