package com.ibrahimahmads.github.vbooth.entity;

import com.ibrahimahmads.github.vbooth.dto.response.AccountResponse;
import com.ibrahimahmads.github.vbooth.entity.base.BaseEntity;
import com.ibrahimahmads.github.vbooth.utils.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "m_account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account extends BaseEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private String name;
    private String password;
    @Enumerated(EnumType.STRING)
    private Roles role;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String googleId;
    private Boolean isEmailVerified;

    public AccountResponse toResponse() {
        return AccountResponse.builder()
                .id(id)
                .name(name)
                .email(email)
                .isEmailVerified(isEmailVerified)
                .hasPassword(this.password != null && !this.password.isBlank())
                .role(role.name())
                .build();
    }
}
