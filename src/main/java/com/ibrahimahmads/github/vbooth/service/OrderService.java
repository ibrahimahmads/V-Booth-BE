package com.ibrahimahmads.github.vbooth.service;

import com.ibrahimahmads.github.vbooth.dto.response.CheckoutResponse;
import com.ibrahimahmads.github.vbooth.entity.Orders;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    CheckoutResponse createOrderByQris(UUID accountId, UUID paketId);
    void expiredPendingOrders();
    Orders getOrderById(UUID id);
    List<Orders> getAllOrders();
    List<Orders> getAllOrdersByAccountId(UUID accountId);
}
