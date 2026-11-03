package com.ibrahimahmads.github.vbooth.service.impl;

import com.ibrahimahmads.github.vbooth.entity.Account;
import com.ibrahimahmads.github.vbooth.service.AccountService;
import com.ibrahimahmads.github.vbooth.service.GoogleAuthService;
import com.ibrahimahmads.github.vbooth.utils.enums.Roles;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GoogleAuthServiceImpl implements GoogleAuthService {
    private final AccountService accountService;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    private final RestClient restClient = RestClient.create();
    private JwtDecoder jwtDecoder;

    @PostConstruct
    void init() {
        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .build();

        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(),
                jwt -> {
                    Object iss = jwt.getClaims().get("iss");
                    boolean issOk = iss != null && (
                            iss.toString().equals("https://accounts.google.com")
                                    || iss.toString().equals("accounts.google.com"));
                    boolean audOk = jwt.getAudience() != null && jwt.getAudience().contains(clientId);
                    return (issOk && audOk)
                            ? OAuth2TokenValidatorResult.success()
                            : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token"));
                });
        decoder.setJwtValidator(validator);
        this.jwtDecoder = decoder;
    }

    @Override
    public Account loginWithCode(String code) {
        Jwt idToken = exchangeAndVerify(code);

        String googleId = idToken.getSubject();
        String email = idToken.getClaimAsString("email").toLowerCase();
        String name = idToken.getClaimAsString("name").toLowerCase();
        boolean emailVerified = Boolean.TRUE.equals(idToken.getClaimAsBoolean("email_verified"));

        if (email == null || !emailVerified) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email Google belum terverifikasi");
        }

        return accountService.getAccountByGoogleId(googleId)
                .orElseGet(() -> accountService.getAccountByEmail(email)
                        .map(existing -> {
                            existing.setGoogleId(googleId);
                            existing.setIsEmailVerified(true);
                            return accountService.save(existing);
                        })
                        .orElseGet(() -> accountService.save(Account.builder()
                                .name(name)
                                .email(email)
                                .googleId(googleId)
                                .isEmailVerified(true)
                                .role(Roles.USER)
                                .build())));
    }

    private Jwt exchangeAndVerify(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("redirect_uri", "postmessage"); // wajib untuk GIS popup flow
        form.add("grant_type", "authorization_code");

        try {
            Map<String, Object> res = restClient.post()
                    .uri("https://oauth2.googleapis.com/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            String idToken = res == null ? null : (String) res.get("id_token");
            if (idToken == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "ID token tidak ditemukan");
            }
            return jwtDecoder.decode(idToken);
        } catch (RestClientException | JwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login Google gagal");
        }
    }
}
