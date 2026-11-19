package com.ecommerce.payment.service;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.ecommerce.payment.DTO.GatewayOrderResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RozarpayPaymentGateway implements PaymentGateway{

	private final RazorpayClient razorpayClient;

	@Override
	public GatewayOrderResponse createOrder(String orderId, BigDecimal amount) {
		
		try {
			JSONObject options = new JSONObject();
			options.put("amount", amount.multiply(BigDecimal.valueOf(100)));
			options.put("currency", "INR");
			options.put("receipt", orderId);
			Order order = razorpayClient.orders.create(options);
			return new GatewayOrderResponse(
					order.get("id"),
					order.get("currency"),
					amount,
					order.get("status"),
					"RAZORPAY");
		} catch (RazorpayException e) {
			 log.error("Razorpay order creation failed", e);

			    throw new IllegalStateException(
			            "Razorpay order creation failed: " + e.getMessage(), e
			    );
		}
	}
}
