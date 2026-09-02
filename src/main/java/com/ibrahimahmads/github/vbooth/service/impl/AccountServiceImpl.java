package com.ibrahimahmads.github.vbooth.service.impl;

import com.ibrahimahmads.github.vbooth.dto.request.ChangePasswordRequest;
import com.ibrahimahmads.github.vbooth.dto.request.SetInitialPasswordRequest;
import com.ibrahimahmads.github.vbooth.dto.request.SetInitialUsernameRequest;
import com.ibrahimahmads.github.vbooth.dto.response.AccountResponse;
import com.ibrahimahmads.github.vbooth.entity.Account;
import com.ibrahimahmads.github.vbooth.repository.AccountRepository;
import com.ibrahimahmads.github.vbooth.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
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
        return accountRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Account getAccountByGoogleId(String googleId) {
        return accountRepository.findByGoogleId(googleId).orElse(null);
    }

    @Override
    public AccountResponse getAccountResponseById(UUID id) {
        return getAccountOrThrowById(id).toResponse();
    }

    @Override
    public Account getAccountEntityById(UUID id) {
        return getAccountOrThrowById(id);
    }

    @Override
    public void setInitialPassword(UUID accountId,SetInitialPasswordRequest payload) {
        Account account = getAccountOrThrowById(accountId);
        account.setPassword(passwordEncoder.encode(payload.password()));
        accountRepository.save(account);
    }

    @Override
    public void changePassword(UUID accountId,ChangePasswordRequest payload) {
        Account account = getAccountOrThrowById(accountId);
        if(!passwordEncoder.matches(payload.oldPassword(), account.getPassword())){
            throw new IllegalArgumentException("Password lama salah");
        }

        if(passwordEncoder.matches(payload.newPassword(), account.getPassword())){
            throw new IllegalArgumentException("Password baru tidak boleh sama dengan password lama");
        }
        account.setPassword(passwordEncoder.encode(payload.newPassword()));
        accountRepository.save(account);
    }

    private Account getAccountOrThrowById(UUID id) {
        return accountRepository.findById(id).orElseThrow(()-> new RuntimeException("account with id: "+id+" not found"));
    }

    @Override
    public void setInitialUsername(UUID accountId, SetInitialUsernameRequest payload) {
        Account account = getAccountOrThrowById(accountId);
        account.setUsername(payload.username());
        accountRepository.save(account);
    }

    @Override
    public List<AccountResponse> getAll() {
        return accountRepository.findAll().stream().map(Account::toResponse).toList();
    }

    @Override
    public void deleteById(UUID id) {
        accountRepository.deleteById(id);
    }
}
