package com.ibrahimahmads.github.vbooth.controller;

import com.ibrahimahmads.github.vbooth.dto.request.CreateAccountRequest;
import com.ibrahimahmads.github.vbooth.dto.request.GoogleAuthRequest;
import com.ibrahimahmads.github.vbooth.dto.request.LoginRequest;
import com.ibrahimahmads.github.vbooth.dto.response.AccountResponse;
import com.ibrahimahmads.github.vbooth.dto.response.LoginResponse;
import com.ibrahimahmads.github.vbooth.service.AuthService;
import com.ibrahimahmads.github.vbooth.utils.enums.Roles;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AccountResponse> loginEmail(@RequestBody LoginRequest payload, @RequestParam Roles loginType) {
        LoginResponse res = authService.login(payload, loginType);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE,res.getJwtCookie().toString())
                .body(res.getAccount());
    }

    @PostMapping("/google")
    public ResponseEntity<AccountResponse> googleLogin(@Valid @RequestBody GoogleAuthRequest request) {
        LoginResponse res = authService.loginWithGoogle(request);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, res.getJwtCookie().toString())
                .body(res.getAccount());
    }
}