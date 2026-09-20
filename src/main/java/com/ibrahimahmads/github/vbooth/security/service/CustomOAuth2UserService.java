package com.ibrahimahmads.github.vbooth.security.service;

import com.ibrahimahmads.github.vbooth.entity.Account;
import com.ibrahimahmads.github.vbooth.repository.AccountRepository;
import com.ibrahimahmads.github.vbooth.security.model.CustomOAuth2User;
import com.ibrahimahmads.github.vbooth.utils.enums.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final AccountRepository accountRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String googleId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        Boolean isEmailVerified = oAuth2User.getAttribute("email_verified");

        Account account = accountRepository.findByGoogleId(googleId)
                .orElseGet(() -> accountRepository.findByEmail(email)
                        .map(existingAccount -> {
                            existingAccount.setGoogleId(googleId);
                            existingAccount.setIsEmailVerified(Boolean.TRUE.equals(isEmailVerified));
                            return accountRepository.save(existingAccount);
                        })
                        .orElseGet(() -> {
                            Account newAccount = Account.builder()
                                    .name(name)
                                    .email(email)
                                    .googleId(googleId)
                                    .isEmailVerified(Boolean.TRUE.equals(isEmailVerified))
                                    .role(Roles.USER)
                                    .build();
                            return accountRepository.save(newAccount);
                        })
                );
        return new CustomOAuth2User(account,oAuth2User.getAttributes());
    }
}
