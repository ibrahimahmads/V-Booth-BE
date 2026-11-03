package com.ibrahimahmads.github.vbooth.service;

import com.ibrahimahmads.github.vbooth.dto.request.ChangePasswordRequest;
import com.ibrahimahmads.github.vbooth.dto.request.EditAccountRequest;
import com.ibrahimahmads.github.vbooth.dto.request.SetInitialPasswordRequest;
import com.ibrahimahmads.github.vbooth.dto.request.SetInitialUsernameRequest;
import com.ibrahimahmads.github.vbooth.dto.response.AccountResponse;
import com.ibrahimahmads.github.vbooth.entity.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    Boolean isEmailExist(String email);
    Boolean isUsernameExist(String username);
    Boolean isGoogleIdExist(String googleId);
    Account save(Account account);
    Optional<Account> getAccountByEmail(String email);
    Optional<Account> getAccountByGoogleId(String googleId);
    AccountResponse getAccountResponseById(UUID id);
    Account getAccountEntityById(UUID id);
    void setInitialPassword(UUID accountId,SetInitialPasswordRequest payload);
    void setInitialUsername(UUID accountId, SetInitialUsernameRequest payload);
    void changePassword(UUID accountId,ChangePasswordRequest payload);
    List<AccountResponse> getAll();
    void deleteById(UUID id);
    Account getAccountByEmailOrUsername(String emailOrUsername);
    AccountResponse editProfile(UUID accountId, EditAccountRequest payload);
}
