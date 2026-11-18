package com.ibrahimahmads.github.vbooth.scheduller;

import com.ibrahimahmads.github.vbooth.service.OrderExpirationService;
import com.ibrahimahmads.github.vbooth.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderScheduler {
    private final OrderExpirationService orderExpirationService;

    @Scheduled(fixedRate = 60000)
    public void expiredOrder(){
        orderExpirationService.expiredPendingOrders();
    }
}
