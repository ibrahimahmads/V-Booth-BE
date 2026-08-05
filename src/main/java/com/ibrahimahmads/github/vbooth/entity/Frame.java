package com.ibrahimahmads.github.vbooth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
}
