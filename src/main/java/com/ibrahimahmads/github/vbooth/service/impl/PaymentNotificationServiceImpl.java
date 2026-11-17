package com.ibrahimahmads.github.vbooth.service.impl;

import com.ibrahimahmads.github.vbooth.dto.request.MidtransNotificationRequest;
import com.ibrahimahmads.github.vbooth.entity.Orders;
import com.ibrahimahmads.github.vbooth.service.OrderService;
import com.ibrahimahmads.github.vbooth.service.PaymentNotificationService;
import com.ibrahimahmads.github.vbooth.utils.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.ibrahimahmads.github.vbooth.event.OrderStatusChangeEvent;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentNotificationServiceImpl implements PaymentNotificationService {
    private final OrderService orderService;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${midtrans.server-key}")
    private String serverKey;

    @Override
    @Transactional
    public void sendPaymentNotification(MidtransNotificationRequest req) {
        if (!isValidSignature(req)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Signature tidak valid");
        }

        Orders order = orderService.getOrderByOrderNumber(req.getOrderId());

        if (!isAmountMatch(order, req.getGrossAmount())) {
            log.error("Nominal tidak cocok untuk {}: order={}, midtrans={}",
                    order.getOrderNumber(), order.getAmount(), req.getGrossAmount());
            return;
        }

        OrderStatus before = order.getStatus();
        String status = req.getTransactionStatus() == null ? "" : req.getTransactionStatus();

        switch (status) {
            case "settlement" -> markPaid(order);
            case "expire" -> {
                if (before == OrderStatus.PENDING) order.setStatus(OrderStatus.EXPIRED);
            }
            case "cancel", "deny" -> {
                if (before == OrderStatus.PENDING) order.setStatus(OrderStatus.FAILED);
            }
            default -> log.info("Status '{}' untuk {} diabaikan", status, order.getOrderNumber());
        }

        if (order.getStatus() != before) {
            eventPublisher.publishEvent(new OrderStatusChangeEvent(order.getOrderNumber(), order.getStatus()));
        }
    }

    private void markPaid(Orders order) {
        if (order.getStatus() == OrderStatus.PAID) {
            return;
        }
        order.setStatus(OrderStatus.PAID);
        order.setPaidAt(LocalDateTime.now());

        // TODO: beri hak paket / buat Event di sini (satu transaksi dengan perubahan status)
    }

    private boolean isAmountMatch(Orders order, String grossAmount) {
        try {
            return new BigDecimal(grossAmount).compareTo(BigDecimal.valueOf(order.getAmount())) == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidSignature(MidtransNotificationRequest r) {
        if (r.getOrderId() == null || r.getStatusCode() == null
                || r.getGrossAmount() == null || r.getSignatureKey() == null) {
            return false;
        }
        try {
            String raw = r.getOrderId() + r.getStatusCode() + r.getGrossAmount() + serverKey;
            byte[] hash = MessageDigest.getInstance("SHA-512").digest(raw.getBytes(StandardCharsets.UTF_8));
            String expected = HexFormat.of().formatHex(hash);
            return MessageDigest.isEqual(
                    expected.getBytes(StandardCharsets.UTF_8),
                    r.getSignatureKey().toLowerCase().getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
