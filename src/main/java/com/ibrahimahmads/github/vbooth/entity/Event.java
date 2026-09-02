package com.ibrahimahmads.github.vbooth.entity;

import com.ibrahimahmads.github.vbooth.entity.base.BaseCreatedEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "t_event")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event extends BaseCreatedEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private String eventName;
    private LocalDate eventDate;
    private LocalTime eventStartTime;
    private LocalTime eventEndTime;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
