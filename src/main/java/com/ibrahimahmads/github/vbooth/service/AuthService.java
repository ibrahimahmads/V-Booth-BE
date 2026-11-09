package com.ibrahimahmads.github.vbooth.service;

import com.ibrahimahmads.github.vbooth.dto.request.CreateAccountRequest;
import com.ibrahimahmads.github.vbooth.dto.request.GoogleAuthRequest;
import com.ibrahimahmads.github.vbooth.dto.request.LoginRequest;
import com.ibrahimahmads.github.vbooth.dto.response.AccountResponse;
import com.ibrahimahmads.github.vbooth.dto.response.LoginResponse;
import com.ibrahimahmads.github.vbooth.utils.enums.Roles;
import org.springframework.http.ResponseCookie;

public interface AuthService {
    void createAccount(CreateAccountRequest payload);
    LoginResponse login(LoginRequest payload, Roles role);
    LoginResponse loginWithGoogle(GoogleAuthRequest payload);
}
