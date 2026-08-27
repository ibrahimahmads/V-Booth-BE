package com.ibrahimahmads.github.vbooth.service;

import com.ibrahimahmads.github.vbooth.dto.request.ChangePasswordRequest;
import com.ibrahimahmads.github.vbooth.dto.request.SetInitialPasswordRequest;
import com.ibrahimahmads.github.vbooth.dto.request.SetInitialUsernameRequest;
import com.ibrahimahmads.github.vbooth.dto.response.AccountResponse;
import com.ibrahimahmads.github.vbooth.entity.Account;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    Boolean isEmailExist(String email);
    Boolean isGoogleIdExist(String googleId);
    Account save(Account account);
    Account getAccountByEmail(String email);
    Account getAccountByGoogleId(String googleId);
    AccountResponse getAccountById(UUID id);
    void setInitialPassword(UUID accountId,SetInitialPasswordRequest payload);
    void setInitialUsername(UUID accountId, SetInitialUsernameRequest payload);
    void changePassword(UUID accountId,ChangePasswordRequest payload);
    List<AccountResponse> getAll();
    void deleteById(UUID id);
}
