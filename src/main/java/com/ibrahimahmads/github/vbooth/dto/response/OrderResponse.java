package com.ibrahimahmads.github.vbooth.dto.response;

import com.ibrahimahmads.github.vbooth.utils.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderResponse {
    private UUID id;
    private String orderNumber;
    private Long amount;
    private OrderStatus orderStatus;
    private String paidAtDay;
    private String paidAtTime;
    private String paymentMethod;
    private String qrUrl;
    private UUID accountId;
    private UUID eventId;
    private String paketName;
}
