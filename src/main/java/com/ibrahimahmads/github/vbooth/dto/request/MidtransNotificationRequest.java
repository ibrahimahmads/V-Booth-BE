package com.ibrahimahmads.github.vbooth.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MidtransNotificationRequest {
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("transaction_id")
    private String transactionId;
    @JsonProperty("transaction_status")
    private String transactionStatus;
    @JsonProperty("status_code")
    private String statusCode;
    @JsonProperty("gross_amount")
    private String grossAmount;
    @JsonProperty("signature_key")
    private String signatureKey;
    @JsonProperty("payment_type")
    private String paymentType;
}
