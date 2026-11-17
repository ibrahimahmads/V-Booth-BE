package com.ibrahimahmads.github.vbooth.scheduller;

import com.ibrahimahmads.github.vbooth.sse.OrderSseRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SseHeartbeatScheduler {
    private final OrderSseRegistry orderSseRegistry;
    @Scheduled(fixedRate = 25000)
    public void heartbeat() {
        orderSseRegistry.heartbeat();
    }
}
