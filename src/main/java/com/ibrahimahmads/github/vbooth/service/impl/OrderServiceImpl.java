package com.ibrahimahmads.github.vbooth.service.impl;

import com.ibrahimahmads.github.vbooth.dto.response.CheckoutResponse;
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
import java.util.List;
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
        Account account = accountService.getAccountEntityById(accountId);
        Paket paket = paketService.getById(paketId);
        if (paket.getPrice() == null || paket.getPrice() <= 0) {
            throw new RuntimeException("Harga paket tidak valid");
        }
        Orders order = Orders.builder()
                .account(account)
                .orderNumber("VB-ORD-"+UUID.randomUUID())
                .paket(paket)
                .status(OrderStatus.PENDING)
                .expiredAt(LocalDateTime.now().plusHours(1))
                .amount(paket.getPrice())
                .build();
        Orders savedOrder = orderRepository.save(order);
        QrisPaymentResponse response = paymentGateway.generateQrisPayment(savedOrder.getOrderNumber(), savedOrder.getAmount());

        return CheckoutResponse.builder()
                .orderId(savedOrder.getId())
                .orderNumber(savedOrder.getOrderNumber())
                .amount(savedOrder.getAmount())
                .orderStatus(savedOrder.getStatus())
                .paymentMethod(response.getPaymentMethod())
                .qrUrl(response.getQrUrl())
                .expiredAt(String.valueOf(response.getExpiredAt()))
                .build();
    }

    @Override
    public Orders getOrderById(UUID id) {
        return orderRepository.findById(id).orElseThrow(()-> new RuntimeException("Order tidak ditemukan"));
    }

    @Override
    @Transactional
    public void expiredPendingOrders() {
        orderRepository.expirePendingOrders(OrderStatus.PENDING,OrderStatus.EXPIRED, LocalDateTime.now());
    }

    @Override
    public List<Orders> getAllOrders() {
        return orderRepository.findAll()    ;
    }

    @Override
    public List<Orders> getAllOrdersByAccountId(UUID accountId) {
        return orderRepository.findAllByAccountId(accountId);
    }
}
