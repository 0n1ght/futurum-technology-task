package com.futurumtechtask.controller;

import com.futurumtechtask.dto.AccountResponse;
import com.futurumtechtask.entity.Account;
import com.futurumtechtask.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/me")
    public AccountResponse getCurrentAccount() {
        Account account = accountService.getCurrentAccount();

        return new AccountResponse(
                account.getUsername(),
                account.getEmeraldBalance()
        );
    }
}
