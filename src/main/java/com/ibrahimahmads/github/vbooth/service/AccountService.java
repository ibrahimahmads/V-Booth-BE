package com.ibrahimahmads.github.vbooth.service;

import com.ibrahimahmads.github.vbooth.entity.Account;

public interface AccountService {
    Boolean isEmailExist(String email);
    Boolean isGoogleIdExist(String googleId);
    void registerAccount(Account account);
    Account getAccountByEmail(String email);
}
