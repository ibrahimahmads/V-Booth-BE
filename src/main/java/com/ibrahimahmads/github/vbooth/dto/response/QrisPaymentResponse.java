package com.ibrahimahmads.github.vbooth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class QrisPaymentResponse {
    private String providerOrderId;
    private String providerTransactionId;
    private String paymentMethod;
    private String qrUrl;
    private LocalDateTime expiredAt;
}
