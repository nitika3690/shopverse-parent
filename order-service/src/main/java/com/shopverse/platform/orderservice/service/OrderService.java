package com.shopverse.platform.orderservice.service;

import com.shopverse.platform.orderservice.dto.OrderRequest;
import com.shopverse.platform.orderservice.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getAllOrders();

    OrderResponse updateOrder(Long id, OrderRequest request);

    void deleteOrder(Long id);
}