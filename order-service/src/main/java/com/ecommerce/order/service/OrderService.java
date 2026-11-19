package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderRequest;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.dto.PlaceOrderRequest;

public interface OrderService {
	OrderResponse PlaceOrder(PlaceOrderRequest request);
}
