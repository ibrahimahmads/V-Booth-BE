package com.ibrahimahmads.github.vbooth.service.impl;

import com.ibrahimahmads.github.vbooth.dto.request.OrderUserSearch;
import com.ibrahimahmads.github.vbooth.dto.response.CheckoutResponse;
import com.ibrahimahmads.github.vbooth.dto.response.OrderDetailResponse;
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
import com.ibrahimahmads.github.vbooth.service.PaymentNotificationService;
import com.ibrahimahmads.github.vbooth.specification.OrderUserSpecification;
import com.ibrahimahmads.github.vbooth.utils.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AccountService accountService;
    private final PaketService paketService;
    private final PaymentGateway paymentGateway;
    private final PaymentNotificationService paymentNotificationService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public CheckoutResponse createOrderByQris(UUID accountId, UUID paketId) {
        Optional<Orders> existing = orderRepository
                .findByAccountIdAndPaketIdAndStatus(accountId, paketId, OrderStatus.PENDING);
        if (existing.isPresent()) {
            Orders old = existing.get();
            if (old.getExpiredAt().isAfter(LocalDateTime.now()) && old.getQrUrl() != null) {
                return toCheckoutResponse(old);
            }

            OrderStatus resolved = resolveStaleOrder(old);
            if (resolved == OrderStatus.PAID) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Pembayaran sebelumnya sudah diterima");
            }
            if (resolved == OrderStatus.PENDING) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Pembayaran sebelumnya sedang diverifikasi, coba lagi beberapa menit");
            }

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
                .expiredAt(LocalDateTime.now().plusMinutes(60))
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
    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderById(UUID id) {
        return orderRepository.findById(id).orElseThrow(()-> new RuntimeException("Order tidak ditemukan")).toDetailResponse();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderByIdAndAccountId(UUID id, UUID accountId) {
        return orderRepository.findByIdAndAccountId(id,accountId).orElseThrow(()-> new RuntimeException("Order tidak ditemukan")).toDetailResponse();
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
    public void expiredOrdersByOrderNumbers(List<String> orderNumbers, OrderStatus expiredStatus, OrderStatus pendingStatus) {
        orderRepository.expireByOrderNumber(orderNumbers, expiredStatus, pendingStatus);
    }

    @Override
    public List<String> getAllOrderNumbersBeforeExpired(OrderStatus status, LocalDateTime expiredAt, int limit) {
        return orderRepository.getAllOrderNumberByStatusAndExpiredAtBefore(status,expiredAt, PageRequest.of(0, limit));
    }

    @Override
    public List<String> getAllOrderNumbersBeforeExpired(OrderStatus status, LocalDateTime expiredAt) {
        return orderRepository.getAllOrderNumberByStatusAndExpiredAtBefore(status,expiredAt);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(Orders::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrdersByAccountId(UUID accountId, Pageable pageable, OrderUserSearch search) {
        Specification<Orders> specification = OrderUserSpecification.getSpecification(accountId, search);
        return orderRepository.findAll(specification,pageable).map(Orders::toResponse);
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

    private OrderStatus resolveStaleOrder(Orders old) {
        try {
            paymentGateway.checkStatus(old.getOrderNumber()).ifPresent(s ->
                    paymentNotificationService.apply(old.getOrderNumber(), s.transactionStatus(), s.grossAmount()));
        } catch (Exception e) {
            log.warn("Gagal cek status Midtrans untuk {}: {}", old.getOrderNumber(), e.getMessage());
        }
        return orderRepository.getStatusByOrderNumber(old.getOrderNumber()).orElse(OrderStatus.PENDING);
    }
}
