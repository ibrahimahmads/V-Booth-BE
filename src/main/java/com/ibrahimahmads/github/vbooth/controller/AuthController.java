package com.ibrahimahmads.github.vbooth.controller;

import com.ibrahimahmads.github.vbooth.dto.request.CreateAccountRequest;
import com.ibrahimahmads.github.vbooth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> createAccount(CreateAccountRequest payload) {
        authService.createAccount(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginEmail(CreateAccountRequest payload) {
        authService.login(payload.email(), payload.password());
        return ResponseEntity.status(HttpStatus.CREATED).body("Login success");
    }
}
