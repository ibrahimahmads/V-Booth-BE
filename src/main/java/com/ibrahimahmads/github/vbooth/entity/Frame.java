package com.ibrahimahmads.github.vbooth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "m_frame")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Frame {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private String nameTheme;
    private String overlayUrl;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
