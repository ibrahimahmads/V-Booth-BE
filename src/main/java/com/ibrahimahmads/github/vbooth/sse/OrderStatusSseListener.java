package com.ibrahimahmads.github.vbooth.sse;

import com.ibrahimahmads.github.vbooth.event.OrderStatusChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderStatusSseListener {
    private final OrderSseRegistry orderSseRegistry;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onStatusChanged(OrderStatusChangeEvent event) {
        orderSseRegistry.broadcast(event.orderNumber(), event.status());
    }
}
