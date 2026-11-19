package com.ecommerce.order.saga;

import java.util.jar.Attributes.Name;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentClient {
@PostMapping("/api/payments/saga")
PaymentResponse createSagaPayment(@RequestBody CreatePaymentRequest request);

@PostMapping("/api/payments/{paymentId}/refund")
void refund(@PathVariable Long paymentId);
}
