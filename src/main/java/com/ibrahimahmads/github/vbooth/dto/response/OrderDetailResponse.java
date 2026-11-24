package com.ibrahimahmads.github.vbooth.dto.response;

import com.ibrahimahmads.github.vbooth.utils.enums.EventStatus;
import com.ibrahimahmads.github.vbooth.utils.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class OrderDetailResponse {
    private UUID id;
    private String orderNumber;
    private Long amount;
    private String qrUrl;
    private OrderStatus status;
    private LocalDateTime paidAt;
    private LocalDateTime expiredAt;
    private String paymentMethod;

    private UUID accountId;
    private String accountName;
    private String accountEmail;

    private PaketInfo paket;
    private EventInfo event;

    @Getter
    @Builder
    public static class PaketInfo {
        private UUID id;
        private String name;
        private Long price;
    }

    @Getter
    @Builder
    public static class EventInfo {
        private UUID id;
        private String name;
        private EventStatus status;
    }
}
