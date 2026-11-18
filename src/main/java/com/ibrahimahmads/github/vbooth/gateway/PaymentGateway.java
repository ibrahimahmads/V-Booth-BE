package com.ibrahimahmads.github.vbooth.gateway;

import com.ibrahimahmads.github.vbooth.dto.response.PaymentStatusResponse;
import com.ibrahimahmads.github.vbooth.dto.response.QrisPaymentResponse;

import java.util.Optional;

public interface PaymentGateway {
    QrisPaymentResponse generateQrisPayment(String orderId,Long amount);
    Optional<PaymentStatusResponse> checkStatus(String orderNumber);
}
