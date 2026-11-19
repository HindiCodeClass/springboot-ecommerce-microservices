package com.ecommerce.order.mapper;

import org.springframework.stereotype.Component;

import com.ecommerce.order.dto.OrderRequest;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.dto.PlaceOrderRequest;
import com.ecommerce.order.order.Order;

@Component
public class OrderMapper {
		public Order toEntity(PlaceOrderRequest request) {
			Order order  =new Order();
			order.setSkuCode(request.getSkuCode());
			order.setQuantity(request.getQuantity());
			order.setPrice(request.getPrice());
			return order;
		}
		public OrderResponse toResponse(Order order) {
			return new OrderResponse(
					order.getOrderNumber(),
					order.getOrderStatus());
		}
}
