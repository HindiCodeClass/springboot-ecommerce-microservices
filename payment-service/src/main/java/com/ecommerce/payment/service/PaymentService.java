package com.ecommerce.payment.service;

import com.ecommerce.payment.DTO.CreatePaymentRequest;
import com.ecommerce.payment.DTO.PaymentResponse;
import com.ecommerce.payment.DTO.PaymentStatusUpdateRequest;
import com.ecommerce.payment.DTO.RequestPayment;

public interface PaymentService {
		PaymentResponse createPayment(RequestPayment request);
		PaymentResponse getPaymentByOrderId(String orderId);
		PaymentResponse updatePaymentStatus(Long PaymentId, PaymentStatusUpdateRequest request);
		PaymentResponse createSagaPayment(CreatePaymentRequest request);
}
