package com.ibrahimahmads.github.vbooth.event;

import com.ibrahimahmads.github.vbooth.utils.enums.OrderStatus;

import java.util.UUID;

public record OrderStatusChangeEvent(String orderNumber, OrderStatus status) {
}
