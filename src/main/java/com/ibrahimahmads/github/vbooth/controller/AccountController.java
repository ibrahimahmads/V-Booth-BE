package com.ibrahimahmads.github.vbooth.controller;

import com.ibrahimahmads.github.vbooth.dto.request.ChangePasswordRequest;
import com.ibrahimahmads.github.vbooth.dto.request.EditAccountRequest;
import com.ibrahimahmads.github.vbooth.dto.request.SetInitialPasswordRequest;
import com.ibrahimahmads.github.vbooth.dto.request.SetInitialUsernameRequest;
import com.ibrahimahmads.github.vbooth.dto.response.AccountResponse;
import com.ibrahimahmads.github.vbooth.security.model.UserContext;
import com.ibrahimahmads.github.vbooth.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/set-initial-password")
    public ResponseEntity<String> setInitialPassword(Authentication authentication,@RequestBody SetInitialPasswordRequest payload) {
        if(authentication.getPrincipal() == null ) throw new NullPointerException("login first to access this resource");
        UserContext userContext = (UserContext) authentication.getPrincipal();
        accountService.setInitialPassword(userContext.getAccountId(),payload);
        return ResponseEntity.ok("Password set successfully");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(Authentication authentication, @RequestBody ChangePasswordRequest payload) {
        if(authentication.getPrincipal() == null ) throw new NullPointerException("login first to access this resource");
        UserContext userContext = (UserContext) authentication.getPrincipal();
        accountService.changePassword(userContext.getAccountId(),payload);
        return ResponseEntity.ok("Password changed successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<AccountResponse> getMyAccount(Authentication authentication){
        if(authentication.getPrincipal() == null ) throw new NullPointerException("login first to access this resource");
        UserContext userContext = (UserContext) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.getAccountResponseById(userContext.getAccountId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountResponse> getMyAccount(@PathVariable UUID id){
        return ResponseEntity.ok(accountService.getAccountResponseById(id));
    }

    @PostMapping("/set-initial-username")
    public ResponseEntity<String> setInitialUsername(Authentication authentication, @RequestBody SetInitialUsernameRequest payload){
        if(authentication.getPrincipal() == null ) throw new NullPointerException("login first to access this resource");
        UserContext userContext = (UserContext) authentication.getPrincipal();
        accountService.setInitialUsername(userContext.getAccountId(), payload);
        return ResponseEntity.ok("Username set successfully");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AccountResponse>> getAllAccounts(){
        return ResponseEntity.ok(accountService.getAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID id){
        accountService.deleteById(id);
        return ResponseEntity.ok("Account deleted successfully");
    }

    @PutMapping("/edit/me")
    public ResponseEntity<AccountResponse> editMyProfil(Authentication authentication, @RequestBody EditAccountRequest payload){
        if(authentication.getPrincipal() == null ) throw new NullPointerException("login first to access this resource");
        UserContext userContext = (UserContext) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(accountService.editProfile(userContext.getAccountId(),payload));
    }
}
