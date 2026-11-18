package com.ibrahimahmads.github.vbooth.service.impl;

import com.ibrahimahmads.github.vbooth.entity.Orders;
import com.ibrahimahmads.github.vbooth.service.OrderService;
import com.ibrahimahmads.github.vbooth.service.OrderStreamService;
import com.ibrahimahmads.github.vbooth.sse.OrderSseRegistry;
import com.ibrahimahmads.github.vbooth.utils.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderStreamServiceImpl implements OrderStreamService {
    private final OrderService orderService;
    private final OrderSseRegistry registry;

    @Value("${order.expire-grace-time}")
    private long graceMinutes;

    @Override
    public SseEmitter subscribe(String orderNumber, UUID accountId) {
        Orders order = orderService.getOrderByOrderNumberAndAccountId(orderNumber, accountId);

        SseEmitter emitter = registry.register(orderNumber, resolveTimeout(order));

        // baca status setelah terdaftar agar event dari webhook tidak terlewat
        OrderStatus current = orderService.getStatusByOrderNumber(orderNumber).orElse(order.getStatus());
        registry.sendTo(orderNumber, emitter, current);

        return emitter;
    }

    private long resolveTimeout(Orders order) {
        if (order.getStatus() != OrderStatus.PENDING) {
            return Duration.ofSeconds(10).toMillis();
        }
        long remaining = Duration.between(LocalDateTime.now(), order.getExpiredAt()).toMillis();
        long buffer = Duration.ofMinutes(graceMinutes + 2).toMillis();   // masa tenggang + jeda scheduler
        return Math.max(remaining + buffer, Duration.ofMinutes(1).toMillis());
    }
}
