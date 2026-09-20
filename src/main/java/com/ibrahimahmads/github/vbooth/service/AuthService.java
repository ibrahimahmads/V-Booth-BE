package com.ibrahimahmads.github.vbooth.service;

import com.ibrahimahmads.github.vbooth.dto.request.CreateAccountRequest;
import com.ibrahimahmads.github.vbooth.dto.request.LoginRequest;
import com.ibrahimahmads.github.vbooth.dto.response.AccountResponse;
import com.ibrahimahmads.github.vbooth.dto.response.LoginResponse;

public interface AuthService {
    void createAccount(CreateAccountRequest payload);
    LoginResponse login(LoginRequest payload);
}
