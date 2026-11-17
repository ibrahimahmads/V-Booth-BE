package com.ibrahimahmads.github.vbooth.service.impl;

import com.ibrahimahmads.github.vbooth.dto.response.CheckoutResponse;
import com.ibrahimahmads.github.vbooth.dto.response.OrderResponse;
import com.ibrahimahmads.github.vbooth.dto.response.QrisPaymentResponse;
import com.ibrahimahmads.github.vbooth.entity.Account;
import com.ibrahimahmads.github.vbooth.entity.Orders;
import com.ibrahimahmads.github.vbooth.entity.Paket;
import com.ibrahimahmads.github.vbooth.gateway.PaymentGateway;
import com.ibrahimahmads.github.vbooth.repository.OrderRepository;
import com.ibrahimahmads.github.vbooth.service.AccountService;
import com.ibrahimahmads.github.vbooth.service.OrderService;
import com.ibrahimahmads.github.vbooth.service.PaketService;
import com.ibrahimahmads.github.vbooth.utils.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AccountService accountService;
    private final PaketService paketService;
    private final PaymentGateway paymentGateway;

    @Override
    public CheckoutResponse createOrderByQris(UUID accountId, UUID paketId) {
        Optional<Orders> existing = orderRepository
                .findByAccountIdAndPaketIdAndStatus(accountId, paketId, OrderStatus.PENDING);
        if (existing.isPresent()) {
            Orders old = existing.get();
            if (old.getExpiredAt().isAfter(LocalDateTime.now()) && old.getQrUrl() != null) {
                return toCheckoutResponse(old);
            }
            old.setStatus(OrderStatus.EXPIRED);
            orderRepository.saveAndFlush(old);
        }
        Account account = accountService.getAccountEntityById(accountId);
        Paket paket = paketService.getById(paketId);
        if (paket.getPrice() == null || paket.getPrice() <= 0) {
            throw new RuntimeException("Harga paket tidak valid");
        }
        String orderNumber = "VB-ORD-"+UUID.randomUUID();
        Orders order = Orders.builder()
                .account(account)
                .orderNumber(orderNumber)
                .paket(paket)
                .status(OrderStatus.PENDING)
                .expiredAt(LocalDateTime.now().plusHours(1))
                .amount(paket.getPrice())
                .build();
        QrisPaymentResponse response;
        try{
            response = paymentGateway.generateQrisPayment(orderNumber, paket.getPrice());
            order.setQrUrl(response.getQrUrl());
            order.setPaymentMethod(response.getPaymentMethod());
        }catch (Exception e){
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            throw new RuntimeException("Gagal membuat order");
        }
        Orders savedOrder = orderRepository.save(order);

        return toCheckoutResponse(savedOrder);
    }

    @Override
    public Orders getOrderById(UUID id) {
        return orderRepository.findById(id).orElseThrow(()-> new RuntimeException("Order tidak ditemukan"));
    }

    @Override
    public Orders getOrderByIdAndAccountId(UUID id, UUID accountId) {
        return orderRepository.findByIdAndAccountId(id,accountId).orElseThrow(()-> new RuntimeException("Order tidak ditemukan"));
    }

    @Override
    public Orders getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findOrdersByOrderNumber(orderNumber).orElseThrow(()-> new RuntimeException("Order tidak ditemukan"));
    }

    @Override
    public Orders getOrderByOrderNumberAndAccountId(String orderNumber, UUID accountId) {
        return orderRepository.findByOrderNumberAndAccountId(orderNumber,accountId).orElseThrow(()-> new RuntimeException("Order tidak ditemukan"));
    }

    @Override
    @Transactional
    public void expiredPendingOrders() {
        orderRepository.expirePendingOrders(OrderStatus.PENDING,OrderStatus.EXPIRED, LocalDateTime.now());
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(Orders::toResponse).toList();
    }

    @Override
    public List<OrderResponse> getAllOrdersByAccountId(UUID accountId) {
        return orderRepository.findAllByAccountId(accountId).stream().map(Orders::toResponse).toList();
    }

    @Override
    public Optional<OrderStatus> getStatusByOrderNumber(String orderNumber) {
        return orderRepository.getStatusByOrderNumber(orderNumber);
    }

    private CheckoutResponse toCheckoutResponse(Orders o) {
        return CheckoutResponse.builder()
                .orderId(o.getId())
                .orderNumber(o.getOrderNumber())
                .amount(o.getAmount())
                .orderStatus(o.getStatus())
                .paymentMethod(o.getPaymentMethod())
                .qrUrl(o.getQrUrl())
                .expiredAt(o.getExpiredAt() == null ? null
                        : o.getExpiredAt().atZone(ZoneId.of("Asia/Jakarta")).toOffsetDateTime().toString())
                .build();
    }
}
