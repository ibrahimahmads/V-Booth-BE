package com.ibrahimahmads.github.vbooth.repository;

import com.ibrahimahmads.github.vbooth.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByGoogleId(String googleId);
    Optional<Account> findByEmail(String email);
    Optional<Account> findByUsernameOrEmail(String username, String email);
    Optional<Account> findByGoogleId(String googleId);
    Optional<Account> findByid(UUID id);
}
