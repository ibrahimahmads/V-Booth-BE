package com.ibrahimahmads.github.vbooth.service.impl;

import com.ibrahimahmads.github.vbooth.dto.request.CreateAccountRequest;
import com.ibrahimahmads.github.vbooth.dto.response.AccountResponse;
import com.ibrahimahmads.github.vbooth.entity.Account;
import com.ibrahimahmads.github.vbooth.repository.AccountRepository;
import com.ibrahimahmads.github.vbooth.service.AuthService;
import com.ibrahimahmads.github.vbooth.utils.enums.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountRepository accountRepository;
    @Override
    public AccountResponse createAccount(CreateAccountRequest payload) {
        if(!accountRepository.existsByEmail(payload.email())){
            throw new IllegalArgumentException("Email sudah terdaftar");
        }

        Account account = Account.builder()
                .name(payload.name())
                .email(payload.email())
                .password(payload.password())
                .role(Roles.USER)
                .emailVerified(false)
                .build();
        return accountRepository.save(account).toResponse();
    }
}
