package com.ibrahimahmads.github.vbooth.entity;

import com.ibrahimahmads.github.vbooth.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "m_paket")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Paket extends BaseEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private String name;
    private String description;
    private Integer eventActivePeriod;
    private Integer galeryActivePeriod;
    private Integer maxGreetings;
    private Integer maxFrame;
    private Long price;
    private Boolean isCanPhotoAndVoice;
    private Boolean isUrlSlug;
}
