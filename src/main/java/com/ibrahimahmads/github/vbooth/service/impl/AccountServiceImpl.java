package com.ibrahimahmads.github.vbooth.service.impl;

import com.ibrahimahmads.github.vbooth.entity.Account;
import com.ibrahimahmads.github.vbooth.repository.AccountRepository;
import com.ibrahimahmads.github.vbooth.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public void registerAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Boolean isEmailExist(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Override
    public Boolean isGoogleIdExist(String googleId) {
        return accountRepository.existsByGoogleId(googleId);
    }

    @Override
    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }
}
