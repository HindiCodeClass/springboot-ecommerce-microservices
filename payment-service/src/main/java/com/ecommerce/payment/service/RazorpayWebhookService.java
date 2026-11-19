package com.ecommerce.payment.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ecommerce.payment.DTO.PaymentEvent;
import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.enums.PaymentStatus;
import com.ecommerce.payment.producer.PaymentEventProducer;
import com.ecommerce.payment.repository.PaymentRepository;
import com.razorpay.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RazorpayWebhookService {
		private final PaymentRepository paymentRepo;
		//private final OrderClient orderClient;
	//	private final InventoryClient inventoryClient;
		private final PaymentEventProducer paymentEventproducer;
		@Value("${razorpay.webhook-secret}")
		private String webhookSecret;
		
		public void processWebhook(String signature, String payload) {
			System.out.println("========== RAZORPAY WEBHOOK ==========");
		    System.out.println("Signature: " + signature);
		    System.out.println("Complete Payload: " + payload);

			verifySignature(signature,payload);
			JSONObject event = new JSONObject(payload);
			String eventType = event.getString("event");
			log.info("Received RazorpayWebhook event {}", eventType);
			
			switch (eventType) {
			case "payment.captured" -> handlePaymentSuccess(event);
			case "payment.failed" -> handlePaymentFailure(event);
			default -> log.warn("unhandled razorpay event ", eventType);
			}
		}
		
		private void handlePaymentSuccess(JSONObject event) {
			JSONObject paymentEntity = extractPaymentEntity(event);
			String razorpayOrderId = paymentEntity.getString("order_id");
			String razorpayPaymentId = paymentEntity.getString("id");
			Payment payment =  paymentRepo.findByGatewayOrderId(razorpayOrderId).orElseThrow(() -> new IllegalStateException("Payment not found for Razorpay orderId"));
		payment.setPaymentStatus(PaymentStatus.SUCCESS);
		payment.setGatewayPaymentId(razorpayPaymentId);
		//orderClient.confirmOrder(payment.getOrderId());
	//	inventoryClient.commitInventory(payment.getOrderId());
		paymentRepo.save(payment);
		paymentEventproducer.publish(PaymentEvent.builder()
				.eventId(UUID.randomUUID().toString())
				.sagaId(payment.getSagaId())
				.orderId(payment.getOrderId())
				.paymentId(payment.getId())
				.gatewayPaymentId(razorpayPaymentId)
				.gatewayOrderId(razorpayOrderId)
				.amount(payment.getAmount())
				.eventType("PAYMENT_SUCCESS")
				.eventTime(LocalDateTime.now()).build()
				);
		}
		private void handlePaymentFailure(JSONObject event) {
			JSONObject paymentEntity =  extractPaymentEntity(event);
			String razorpayOrderId =  paymentEntity.getString("order_id");
			String razorpayPaymentId = paymentEntity.optString("id", null);
			Payment payment = paymentRepo.findByGatewayOrderId(razorpayOrderId).orElseThrow(()-> new IllegalStateException("Payment not found for this razorpay order id"));
		payment.setPaymentStatus(PaymentStatus.FAILED);
		paymentRepo.save(payment);
		paymentEventproducer.publish(PaymentEvent.builder()
				.eventId(UUID.randomUUID().toString())
				.sagaId(payment.getSagaId())
				.orderId(payment.getOrderId())
				.paymentId(payment.getId())
				.gatewayPaymentId(razorpayPaymentId)
				.gatewayOrderId(razorpayOrderId)
				.amount(payment.getAmount())
				.eventType("PAYMENT_FAILED")
				.eventTime(LocalDateTime.now()).build()
				);
		}
		private JSONObject extractPaymentEntity(JSONObject event) {
			return event.getJSONObject("payload")
					.getJSONObject("payment")
					.getJSONObject("entity");
		}
		
		private void verifySignature(String signature, String payload) {
			try {
				Utils.verifyWebhookSignature(payload, signature, webhookSecret);
			} catch (Exception e) {
				log.error("invalid razorpay webhook signature");
				throw new SecurityException("Invalid razorpay webhook signature");
			}
		}
		
		
}
