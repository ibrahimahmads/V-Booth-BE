package com.ibrahimahmads.github.vbooth.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ibrahimahmads.github.vbooth.dto.response.EventResponse;
import com.ibrahimahmads.github.vbooth.entity.base.BaseCreatedEntity;
import com.ibrahimahmads.github.vbooth.utils.enums.EventStatus;
import com.ibrahimahmads.github.vbooth.utils.enums.EventTypes;
import com.ibrahimahmads.github.vbooth.utils.helper.DateUtil;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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
    @Enumerated(EnumType.STRING)
    private EventTypes eventTypes;
    private String eventDescription;
    private String eventLocation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("event")
    private List<Frame> frameList = new ArrayList<>();
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("event")
    private List<Greetings> greetingsList = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paket_id")
    private Paket paket;

    public EventResponse toResponse(){
        return EventResponse.builder()
                .id(id)
                .eventName(eventName)
                .slug(slug)
                .eventDescription(eventDescription)
                .eventStatus(eventStatus)
                .eventLocation(eventLocation)
                .numberOfGreetings(greetingsList.size())
                .eventDate(DateUtil.formatDate(eventDate))
                .eventStartTime(DateUtil.formatTime(eventStartTime))
                .eventEndTime(DateUtil.formatTime(eventEndTime))
                .paket(EventResponse.PaketInfo.builder()
                        .id(paket.getId())
                        .name(paket.getName())
                        .maxGuest(paket.getMaxGuest())
                        .build())
                .build();
    }
}
