package com.ibrahimahmads.github.vbooth.entity;

import com.ibrahimahmads.github.vbooth.dto.response.GreetingsResponse;
import com.ibrahimahmads.github.vbooth.utils.DateUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "m_greeting")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Greetings {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private String guestName;
    private String photoUrl;
    private String audioUrl;
    private LocalDateTime createdAt;

    public GreetingsResponse toResponse() {
        return GreetingsResponse.builder()
                .guestName(guestName)
                .photoUrl(photoUrl)
                .audioUrl(audioUrl)
                .tgl(DateUtil.formatDate(createdAt))
                .jam(DateUtil.formatTime(createdAt))
                .build();
    }
}
