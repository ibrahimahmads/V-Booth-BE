package com.ibrahimahmads.github.vbooth.security.service;

import com.ibrahimahmads.github.vbooth.entity.Account;
import com.ibrahimahmads.github.vbooth.repository.AccountRepository;
import com.ibrahimahmads.github.vbooth.security.model.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        Account acc = accountRepository.findByEmail(input)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with email: " + input));

        GrantedAuthority authorities = new SimpleGrantedAuthority("ROLE_"+acc.getRole().name());

        return new UserContext(
                acc.getId(),
                acc.getEmail(),
                acc.getPassword(),
                Collections.singletonList(authorities));
    }
}

