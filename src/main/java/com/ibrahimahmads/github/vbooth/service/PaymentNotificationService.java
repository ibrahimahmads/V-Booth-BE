package com.ibrahimahmads.github.vbooth.service;

import com.ibrahimahmads.github.vbooth.dto.request.MidtransNotificationRequest;

public interface PaymentNotificationService {
    void sendPaymentNotification(MidtransNotificationRequest request);
    void apply(String orderNumber, String transactionStatus,String amount);
}
