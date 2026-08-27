package com.ibrahimahmads.github.vbooth.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.ibrahimahmads.github.vbooth.dto.request.CreateAccountRequest;
import com.ibrahimahmads.github.vbooth.dto.response.AccountResponse;
import com.ibrahimahmads.github.vbooth.entity.Account;
import com.ibrahimahmads.github.vbooth.service.AccountService;
import com.ibrahimahmads.github.vbooth.service.AuthService;
import com.ibrahimahmads.github.vbooth.service.GoogleAuthService;
import com.ibrahimahmads.github.vbooth.utils.enums.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountService accountService;
    private final GoogleAuthService googleAuthService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void createAccount(CreateAccountRequest payload) {
        if(accountService.isEmailExist(payload.email())){
            throw new IllegalArgumentException("Email sudah terdaftar");
        }

        Account account = Account.builder()
                .name(payload.name())
                .username(payload.username())
                .email(payload.email())
                .password(passwordEncoder.encode(payload.password()))
                .role(Roles.USER)
                .isEmailVerified(false)
                .build();
        accountService.save(account);
    }

    @Override
    public AccountResponse authenticateWithGoogle(String googleId) {
        GoogleIdToken.Payload payload = googleAuthService.verifyAndExtractToken(googleId);
        Account existingAccountByGoogleId = accountService.getAccountByGoogleId(payload.getSubject());
        if(existingAccountByGoogleId != null){
            return existingAccountByGoogleId.toResponse();
        }else{
            Account existingAccountByEmail = accountService.getAccountByEmail(payload.getEmail());
            if(existingAccountByEmail != null){
                existingAccountByEmail.setGoogleId(payload.getSubject());
                existingAccountByEmail.setIsEmailVerified(payload.getEmailVerified());
                accountService.save(existingAccountByEmail);
                return existingAccountByEmail.toResponse();
            }else{
                Account account = Account.builder()
                        .name(payload.get("name").toString())
                        .email(payload.getEmail())
                        .googleId(payload.getSubject())
                        .isEmailVerified(payload.getEmailVerified())
                        .role(Roles.USER)
                        .build();
                Account savedAccount = accountService.save(account);
                return savedAccount.toResponse();
            }
        }
    }

    @Override
    public AccountResponse login(String email, String password) {
        Account account = accountService.getAccountByEmail(email);
        if(account == null || !passwordEncoder.matches(password, account.getPassword())){
            throw new IllegalArgumentException("Email atau password salah");
        }
        return account.toResponse();
    }
}
