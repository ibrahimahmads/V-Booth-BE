package com.ibrahimahmads.github.vbooth.service.impl;

import com.ibrahimahmads.github.vbooth.dto.request.CreateAccountRequest;
import com.ibrahimahmads.github.vbooth.dto.request.LoginRequest;
import com.ibrahimahmads.github.vbooth.dto.response.LoginResponse;
import com.ibrahimahmads.github.vbooth.entity.Account;
import com.ibrahimahmads.github.vbooth.security.jwt.JwtUtil;
import com.ibrahimahmads.github.vbooth.service.AccountService;
import com.ibrahimahmads.github.vbooth.service.AuthService;
import com.ibrahimahmads.github.vbooth.utils.enums.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void createAccount(CreateAccountRequest payload) {
        if(accountService.isEmailExist(payload.email())){
            throw new IllegalArgumentException("Email sudah terdaftar");
        }

        if(accountService.isUsernameExist(payload.username())){
            throw new IllegalArgumentException("Username sudah terdaftar");
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
    public LoginResponse login(LoginRequest payload) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        payload.usernameOrEmail(), payload.password()
                )
        );
        Account account = accountService.getAccountByEmailOrUsername(payload.usernameOrEmail());
        String token = jwtUtil.generateToken(authentication);
        ResponseCookie cookie = jwtUtil.generateJwtCookie(token);
        return LoginResponse.builder()
                .account(account.toResponse())
                .jwtCookie(cookie)
                .build();
    }
}
