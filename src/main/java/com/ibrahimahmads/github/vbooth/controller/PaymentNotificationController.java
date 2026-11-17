package com.ibrahimahmads.github.vbooth.controller;

import com.ibrahimahmads.github.vbooth.dto.request.MidtransNotificationRequest;
import com.ibrahimahmads.github.vbooth.service.PaymentNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentNotificationController {
    private final PaymentNotificationService paymentNotificationService;

    @PostMapping("/midtrans/notification")
    public ResponseEntity<Void> notification(@RequestBody MidtransNotificationRequest request) {
        paymentNotificationService.sendPaymentNotification(request);
        return ResponseEntity.ok().build();
    }
}
