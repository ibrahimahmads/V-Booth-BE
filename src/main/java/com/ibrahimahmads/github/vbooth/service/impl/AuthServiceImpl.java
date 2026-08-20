package com.ibrahimahmads.github.vbooth.service.impl;

import com.ibrahimahmads.github.vbooth.dto.request.CreateAccountRequest;
import com.ibrahimahmads.github.vbooth.dto.response.AccountResponse;
import com.ibrahimahmads.github.vbooth.entity.Account;
import com.ibrahimahmads.github.vbooth.service.AccountService;
import com.ibrahimahmads.github.vbooth.service.AuthService;
import com.ibrahimahmads.github.vbooth.utils.enums.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void createAccount(CreateAccountRequest payload) {
        if(!accountService.isEmailExist(payload.email())){
            throw new IllegalArgumentException("Email sudah terdaftar");
        }

        Account account = Account.builder()
                .name(payload.name())
                .email(payload.email())
                .password(passwordEncoder.encode(payload.password()))
                .role(Roles.USER)
                .emailVerified(false)
                .build();
        accountService.registerAccount(account);
    }

    @Override
    public AccountResponse createAccountWithGoogle(String googleId) {
        return null;
    }

    @Override
    public AccountResponse login(String email, String password) {
        Account account = accountService.getAccountByEmail(email);
        if(account == null || passwordEncoder.matches(password, account.getPassword())){
            throw new IllegalArgumentException("Email atau password salah");
        }
        return account.toResponse();
    }
}
