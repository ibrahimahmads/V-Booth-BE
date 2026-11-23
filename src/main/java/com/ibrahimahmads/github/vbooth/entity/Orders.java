package com.ibrahimahmads.github.vbooth.entity;

import com.ibrahimahmads.github.vbooth.dto.response.OrderResponse;
import com.ibrahimahmads.github.vbooth.entity.base.BaseCreatedEntity;
import com.ibrahimahmads.github.vbooth.utils.enums.OrderStatus;
import com.ibrahimahmads.github.vbooth.utils.helper.DateUtil;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "t_orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Orders extends BaseCreatedEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    @Column(nullable = false, unique = true)
    private String orderNumber;
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    private LocalDateTime paidAt;

    @Column(length = 1000)
    private String qrUrl;

    @Column(length = 50)
    private String paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", unique = true)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paket_id", nullable = false)
    private Paket paket;

    public OrderResponse toResponse(){
        return OrderResponse.builder()
                .id(id)
                .orderNumber(orderNumber)
                .amount(amount)
                .orderStatus(status)
                .paidAtDay(DateUtil.formatDate(paidAt))
                .paidAtTime(DateUtil.formatTime(paidAt))
                .qrUrl(qrUrl)
                .paymentMethod(paymentMethod)
                .accountId(account.getId())
                .eventId(event==null? null: event.getId())
                .paketName(paket.getName())
                .build();
    }
}
