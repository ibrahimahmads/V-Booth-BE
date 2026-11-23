package com.ibrahimahmads.github.vbooth.gateway;

import com.ibrahimahmads.github.vbooth.dto.response.PaymentStatusResponse;
import com.ibrahimahmads.github.vbooth.dto.response.QrisPaymentResponse;
import com.ibrahimahmads.github.vbooth.dto.response.midtrans.ActionResponse;
import com.ibrahimahmads.github.vbooth.dto.response.midtrans.MidtransResponse;
import com.ibrahimahmads.github.vbooth.dto.response.midtrans.MidtransStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MidtransPaymentGateway implements PaymentGateway{
    private final RestTemplate restTemplate;
    @Value("${midtrans.server-key}")
    private String serverKey;
    @Value("${midtrans.base-url}")
    private String baseUrl;

    private static final DateTimeFormatter MIDTRANS_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public QrisPaymentResponse generateQrisPayment(String orderId, Long amount) {
        String url = baseUrl + "/v2/charge";
        Map<String, Object> requestBody = Map.of(
                "payment_type", "qris",
                "transaction_details", Map.of(
                        "order_id", orderId,
                        "gross_amount", amount
                ),
                "qris", Map.of(
                        "acquirer", "gopay"
                ),
                "custom_expiry", Map.of(
                        "expiry_duration", 3,
                        "unit", "minute"
                )
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(serverKey,"");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<MidtransResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                MidtransResponse.class
        );

        MidtransResponse midtrans = response.getBody();

        if (midtrans == null) {
            throw new IllegalStateException(
                    "Response Midtrans kosong"
            );
        }
        if (!"201".equals(midtrans.getStatusCode())) {
            throw new IllegalStateException("Midtrans menolak: " + midtrans.getStatusMessage());
        }

        String qrUrl = midtrans.getActions()
                .stream()
                .filter(action ->
                        "generate-qr-code".equals(action.getName())
                )
                .map(ActionResponse::getUrl)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "URL QRIS tidak ditemukan"
                ));

        LocalDateTime expiredAt = null;

        if (midtrans.getExpiryTime() != null
                && !midtrans.getExpiryTime().isBlank()) {

            expiredAt = LocalDateTime.parse(
                    midtrans.getExpiryTime(),
                    MIDTRANS_DATE_FORMAT
            );
        }

        return QrisPaymentResponse.builder()
                .providerOrderId(midtrans.getOrderId())
                .providerTransactionId(midtrans.getTransactionId())
                .paymentMethod(midtrans.getPaymentType())
                .qrUrl(qrUrl)
                .expiredAt(expiredAt)
                .build();
    }

    @Override
    public Optional<PaymentStatusResponse> checkStatus(String orderNumber) {
        String url = baseUrl + "/v2/" + orderNumber + "/status";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(serverKey, "");

        try {
            ResponseEntity<MidtransStatusResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    MidtransStatusResponse.class
            );

            MidtransStatusResponse body = response.getBody();
            if (body == null
                    || "404".equals(body.getStatusCode())      // Midtrans bisa membalas HTTP 200 dengan status_code 404 di body
                    || body.getTransactionStatus() == null) {
                return Optional.empty();
            }
            return Optional.of(new PaymentStatusResponse(body.getTransactionStatus(), body.getGrossAmount()));
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }
}
