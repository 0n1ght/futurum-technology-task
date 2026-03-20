package com.futurumtechtask.controller;

import com.futurumtechtask.dto.AccountDTO;
import com.futurumtechtask.entity.Account;
import com.futurumtechtask.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AccountService accountService;

    @PostMapping("/register")
    public Account register(@RequestBody AccountDTO request) {
        return accountService.register(request);
    }
}
