package com.ibrahimahmads.github.vbooth.service.impl;

import com.ibrahimahmads.github.vbooth.dto.request.MidtransNotificationRequest;
import com.ibrahimahmads.github.vbooth.dto.response.PaymentStatusResponse;
import com.ibrahimahmads.github.vbooth.entity.Orders;
import com.ibrahimahmads.github.vbooth.gateway.PaymentGateway;
import com.ibrahimahmads.github.vbooth.repository.OrderRepository;
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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentNotificationServiceImpl implements PaymentNotificationService {
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PaymentGateway paymentGateway;

    @Value("${midtrans.server-key}")
    private String serverKey;

    @Override
    @Transactional
    public void sendPaymentNotification(MidtransNotificationRequest req) {
//        if (!isValidSignature(req)) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Signature tidak valid");
//        }

        if (req.getOrderId() == null || req.getOrderId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "order_id kosong");
        }

        // Status diambil langsung dari Midtrans, bukan dari isi request.
        // Kalau Midtrans error atau timeout, exception dilempar (500) sehingga Midtrans mengirim ulang.
        PaymentStatusResponse verified = paymentGateway.checkStatus(req.getOrderId())
                .orElseThrow(() -> {
                    log.warn("Midtrans tidak mengenal order {}", req.getOrderId());
                    return new ResponseStatusException(HttpStatus.FORBIDDEN, "Transaksi tidak ditemukan");
                });
        apply(req.getOrderId(), verified.transactionStatus(), verified.grossAmount());
    }

    @Override
    @Transactional
    public void apply(String orderNumber, String transactionStatus,String amount) {
        Optional<Orders> optional = orderRepository.findOrdersByOrderNumber(orderNumber);
        if (optional.isEmpty()) {
            log.warn("Status untuk order tidak dikenal: {}", orderNumber);
            return;
        }
        Orders order = optional.get();

        if (!isAmountMatch(order, amount)) {
            log.error("Nominal tidak cocok untuk {}: order={}, midtrans={}",
                    order.getOrderNumber(), order.getAmount(), amount);
            return;
        }

        OrderStatus before = order.getStatus();
        String status = transactionStatus == null ? "" : transactionStatus;

        switch (status) {
            case "settlement" -> markPaid(order);
            case "expire" -> {
                if (before == OrderStatus.PENDING) order.setStatus(OrderStatus.EXPIRED);
            }
            case "cancel", "deny" -> {
                if (before == OrderStatus.PENDING) order.setStatus(OrderStatus.FAILED);
            }
            default -> log.debug("Status '{}' untuk {} diabaikan", status, order.getOrderNumber());
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

//    private boolean isValidSignature(MidtransNotificationRequest r) {
//        if (r.getOrderId() == null || r.getStatusCode() == null
//                || r.getGrossAmount() == null || r.getSignatureKey() == null) {
//            return false;
//        }
//        try {
//            String raw = r.getOrderId() + r.getStatusCode() + r.getGrossAmount() + serverKey;
//            byte[] hash = MessageDigest.getInstance("SHA-512").digest(raw.getBytes(StandardCharsets.UTF_8));
//            String expected = HexFormat.of().formatHex(hash);
//            log.info("sig diterima = {}", r.getSignatureKey());
//            log.info("sig dihitung = {}", expected);
//            log.info("raw tanpa key = [{}]", r.getOrderId() + r.getStatusCode() + r.getGrossAmount());
//            log.info("panjang server key = {}", serverKey == null ? "null" : serverKey.length());
//            return MessageDigest.isEqual(
//                    expected.getBytes(StandardCharsets.UTF_8),
//                    r.getSignatureKey().toLowerCase().getBytes(StandardCharsets.UTF_8));
//        } catch (NoSuchAlgorithmException e) {
//            throw new IllegalStateException(e);
//        }
//    }
}
