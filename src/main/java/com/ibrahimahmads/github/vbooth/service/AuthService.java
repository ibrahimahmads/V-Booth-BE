package com.ibrahimahmads.github.vbooth.service;

import com.ibrahimahmads.github.vbooth.dto.request.CreateAccountRequest;
import com.ibrahimahmads.github.vbooth.dto.response.AccountResponse;

public interface AuthService {
    void createAccount(CreateAccountRequest payload);
    AccountResponse createAccountWithGoogle(String googleId);
    AccountResponse login(String email, String password);
}
