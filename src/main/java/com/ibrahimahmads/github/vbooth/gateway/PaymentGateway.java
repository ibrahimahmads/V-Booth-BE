package com.ibrahimahmads.github.vbooth.gateway;

import com.ibrahimahmads.github.vbooth.dto.response.QrisPaymentResponse;

public interface PaymentGateway {
    QrisPaymentResponse generateQrisPayment(String orderId,Long amount);
}
