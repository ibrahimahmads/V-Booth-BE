package com.ibrahimahmads.github.vbooth.controller;

import com.ibrahimahmads.github.vbooth.dto.request.CreateAccountRequest;
import com.ibrahimahmads.github.vbooth.dto.request.LoginRequest;
import com.ibrahimahmads.github.vbooth.dto.response.AccountResponse;
import com.ibrahimahmads.github.vbooth.dto.response.LoginResponse;
import com.ibrahimahmads.github.vbooth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> createAccount(@RequestBody CreateAccountRequest payload) {
        authService.createAccount(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AccountResponse> loginEmail(@RequestBody LoginRequest payload) {
        LoginResponse loginResponse = authService.login(payload);
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE,loginResponse.getJwtCookie().toString()).body(loginResponse.getAccount());
    }
}