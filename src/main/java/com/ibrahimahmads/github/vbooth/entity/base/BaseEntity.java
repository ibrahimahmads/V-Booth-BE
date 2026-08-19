package com.ibrahimahmads.github.vbooth.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseCreatedEntity{
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updateAt;
}
