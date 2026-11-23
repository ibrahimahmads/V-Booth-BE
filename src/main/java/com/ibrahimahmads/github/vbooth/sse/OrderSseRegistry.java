package com.ibrahimahmads.github.vbooth.sse;

import com.ibrahimahmads.github.vbooth.utils.enums.OrderStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class OrderSseRegistry {
    private static final int MAX_PER_ORDER = 3;
    private final Map<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();
    public SseEmitter register(String orderNumber, long timeoutMs) {
        SseEmitter emitter = new SseEmitter(timeoutMs);

        emitters.compute(orderNumber, (k, list) -> {
            if (list == null) list = new CopyOnWriteArrayList<>();
            if (list.size() >= MAX_PER_ORDER) {
                SseEmitter oldest = list.removeFirst();   // batasi koneksi per order
                oldest.complete();
            }
            list.add(emitter);
            return list;
        });

        emitter.onCompletion(() -> remove(orderNumber, emitter));
        emitter.onTimeout(emitter::complete);
        emitter.onError(e -> remove(orderNumber, emitter));
        return emitter;
    }

    public void sendTo(String orderNumber, SseEmitter emitter, OrderStatus status) {
        try {
            emitter.send(buildEvent(orderNumber, status));
            if (status != OrderStatus.PENDING) {
                emitter.complete();
            }
        } catch (Exception e) {
            remove(orderNumber, emitter);
        }
    }

    public void broadcast(String orderNumber, OrderStatus status) {
        List<SseEmitter> list = emitters.get(orderNumber);
        if (list == null) return;
        for (SseEmitter emitter : list) {
            sendTo(orderNumber, emitter, status);
        }
    }

    public void heartbeat() {
        emitters.forEach((orderId, list) -> {
            for (SseEmitter emitter : list) {
                try {
                    emitter.send(SseEmitter.event().comment("ping"));
                } catch (Exception e) {
                    remove(orderId, emitter);
                }
            }
        });
    }

    private void remove(String orderNumber, SseEmitter emitter) {
        emitters.computeIfPresent(orderNumber, (k, list) -> {
            list.remove(emitter);
            return list.isEmpty() ? null : list;
        });
    }

    private SseEmitter.SseEventBuilder buildEvent(String orderNumber, OrderStatus status) {
        return SseEmitter.event()
                .name("order-status")
                .data(Map.of("orderNumber", orderNumber, "status", status.name()));
    }
}
