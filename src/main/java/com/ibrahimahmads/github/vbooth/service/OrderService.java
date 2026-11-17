package com.ibrahimahmads.github.vbooth.service;

import com.ibrahimahmads.github.vbooth.dto.response.CheckoutResponse;
import com.ibrahimahmads.github.vbooth.dto.response.OrderResponse;
import com.ibrahimahmads.github.vbooth.entity.Orders;
import com.ibrahimahmads.github.vbooth.utils.enums.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    CheckoutResponse createOrderByQris(UUID accountId, UUID paketId);
    void expiredPendingOrders();
    Orders getOrderById(UUID id);
    Orders getOrderByOrderNumber(String orderNumber);
    Orders getOrderByIdAndAccountId(UUID id, UUID accountId);
    Orders getOrderByOrderNumberAndAccountId(String orderNumber, UUID accountId);
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getAllOrdersByAccountId(UUID accountId);
    Optional<OrderStatus> getStatusByOrderNumber(String orderNumber);
}
