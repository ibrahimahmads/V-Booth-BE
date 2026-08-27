package com.ibrahimahmads.github.vbooth.security.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;
import java.util.UUID;

@Getter
public class UserContext extends User {
    private final UUID accountId;

    public UserContext(UUID accountId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.accountId = accountId;
    }
}

