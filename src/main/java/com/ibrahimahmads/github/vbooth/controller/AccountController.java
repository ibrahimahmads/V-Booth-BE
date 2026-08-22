package com.ibrahimahmads.github.vbooth.controller;

import com.ibrahimahmads.github.vbooth.dto.request.ChangePasswordRequest;
import com.ibrahimahmads.github.vbooth.dto.request.SetInitialPasswordRequest;
import com.ibrahimahmads.github.vbooth.dto.response.AccountResponse;
import com.ibrahimahmads.github.vbooth.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("set-initial-password")
    public ResponseEntity<String> setInitialPassword(@RequestBody SetInitialPasswordRequest payload) {
        accountService.setInitialPassword(payload);
        return ResponseEntity.ok("Password set successfully");
    }

    @PostMapping("change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest payload) {
        accountService.changePassword(payload);
        return ResponseEntity.ok("Password changed successfully");
    }

    @GetMapping("me")
    public ResponseEntity<AccountResponse> getMyAccountById(UUID id){
        return ResponseEntity.ok(accountService.getAccountById(id));
    }
}
