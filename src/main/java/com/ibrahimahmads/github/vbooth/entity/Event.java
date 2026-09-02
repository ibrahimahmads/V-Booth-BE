package com.ibrahimahmads.github.vbooth.entity;

import com.ibrahimahmads.github.vbooth.entity.base.BaseCreatedEntity;
import com.ibrahimahmads.github.vbooth.utils.enums.EventStatus;
import com.ibrahimahmads.github.vbooth.utils.enums.EventTypes;
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
    @Column(unique = true)
    private String slug;
    private LocalDate eventDate;
    private LocalTime eventStartTime;
    private LocalTime eventEndTime;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;
    private String eventDescription;
    private String eventLocation;
    @Enumerated(EnumType.STRING)
    private EventTypes eventType;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
