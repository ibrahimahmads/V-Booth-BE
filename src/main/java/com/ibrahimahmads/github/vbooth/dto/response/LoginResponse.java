package com.ibrahimahmads.github.vbooth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseCookie;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginResponse {
    AccountResponse account;
    ResponseCookie jwtCookie;
}
