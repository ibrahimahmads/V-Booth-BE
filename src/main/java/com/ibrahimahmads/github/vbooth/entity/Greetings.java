package com.ibrahimahmads.github.vbooth.entity;

import com.ibrahimahmads.github.vbooth.dto.response.GreetingsResponse;
import com.ibrahimahmads.github.vbooth.entity.base.BaseCreatedEntity;
import com.ibrahimahmads.github.vbooth.utils.helper.DateUtil;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "m_greeting")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Greetings extends BaseCreatedEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private String guestName;
    private String photoUrl;
    private String audioUrl;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    public GreetingsResponse toResponse() {
        return GreetingsResponse.builder()
                .id(id)
                .guestName(guestName)
                .photoUrl(photoUrl)
                .audioUrl(audioUrl)
                .tgl(DateUtil.formatDate(this.getCreatedAt()))
                .jam(DateUtil.formatTime(this.getCreatedAt()))
                .build();
    }
}
