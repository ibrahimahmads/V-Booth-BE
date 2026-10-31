package com.ibrahimahmads.github.vbooth.service;

import com.ibrahimahmads.github.vbooth.entity.Account;

public interface GoogleAuthService {
    Account loginWithCode(String code);
}
