package com.ecommerce.order.saga;

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
public class PaymentResponse {

	private Long paymentId;
	private String orderId;
	private String razorpayOrderId;
	private String status;
}
