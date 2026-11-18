package com.ibrahimahmads.github.vbooth.dto.response.midtrans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MidtransStatusResponse {
    @JsonProperty("status_code")
    private String statusCode;
    @JsonProperty("status_message")
    private String statusMessage;
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("transaction_status")
    private String transactionStatus;
    @JsonProperty("gross_amount")
    private String grossAmount;
}
