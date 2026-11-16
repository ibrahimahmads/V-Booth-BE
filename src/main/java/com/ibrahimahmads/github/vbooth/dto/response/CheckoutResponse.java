package com.ibrahimahmads.github.vbooth.dto.response;

import com.ibrahimahmads.github.vbooth.utils.enums.OrderStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponse {
    private UUID orderId;
    private String orderNumber;
    private Long amount;
    private OrderStatus orderStatus;
    private String paymentMethod;
    private String qrUrl;
    private String expiredAt;
}
