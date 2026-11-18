package com.ibrahimahmads.github.vbooth.service.impl;

import com.ibrahimahmads.github.vbooth.event.OrderStatusChangeEvent;
import com.ibrahimahmads.github.vbooth.gateway.PaymentGateway;
import com.ibrahimahmads.github.vbooth.service.OrderExpirationService;
import com.ibrahimahmads.github.vbooth.service.OrderService;
import com.ibrahimahmads.github.vbooth.service.PaymentNotificationService;
import com.ibrahimahmads.github.vbooth.utils.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderExpirationServiceImpl implements OrderExpirationService {
    private static final int BATCH_SIZE = 50;
    private final OrderService orderService;
    private final PaymentGateway paymentGateway;
    private final PaymentNotificationService paymentNotificationService;
    private final ApplicationEventPublisher eventPublisher;
    private final TransactionTemplate transactionTemplate;

    @Value("${order.expire-grace-time}")
    private long graceMinutes;

    @Override
    public void expiredPendingOrders() {
        reconcileWithMidtrans();
        forceExpireAfterGrace();
    }

    private void reconcileWithMidtrans() {
        List<String> orderNumbers = orderService.getAllOrderNumbersBeforeExpired(
                OrderStatus.PENDING, LocalDateTime.now(), BATCH_SIZE);

        for (String orderNumber : orderNumbers) {
            try {
                paymentGateway.checkStatus(orderNumber).ifPresent(s ->
                        paymentNotificationService.apply(orderNumber, s.transactionStatus(), s.grossAmount()));
            } catch (Exception e) {
                // jangan hentikan order lain; dicoba lagi pada putaran berikutnya
                log.warn("Gagal cek status Midtrans untuk {}: {}", orderNumber, e.getMessage());
            }
        }
    }

    private void forceExpireAfterGrace() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(graceMinutes);

        // TransactionTemplate: event SSE (AFTER_COMMIT) hanya jalan kalau ada transaksi aktif
        transactionTemplate.executeWithoutResult(status -> {
            List<String> orderNumbers = orderService.getAllOrderNumbersBeforeExpired(OrderStatus.PENDING, threshold);
            if (orderNumbers.isEmpty()) return;

            orderService.expiredOrdersByOrderNumbers(orderNumbers, OrderStatus.EXPIRED, OrderStatus.PENDING);
            orderNumbers.forEach(id -> eventPublisher.publishEvent(new OrderStatusChangeEvent(id, OrderStatus.EXPIRED)));
        });
    }
}
