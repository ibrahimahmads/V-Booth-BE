package com.ibrahimahmads.github.vbooth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AccountResponse {
    private UUID id;
    private String name;
    private String email;
    private Boolean isEmailVerified;
    private Boolean hasPassword;
    private String role;
}
