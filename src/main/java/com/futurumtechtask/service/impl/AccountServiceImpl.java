package com.futurumtechtask.service.impl;

import com.futurumtechtask.dto.AccountDTO;
import com.futurumtechtask.entity.Account;
import com.futurumtechtask.repo.AccountRepo;
import com.futurumtechtask.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepo accountRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Account getCurrentAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        String username = auth.getName();

        return accountRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public Account register(AccountDTO request) {
        if (accountRepo.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setEmeraldBalance(BigDecimal.valueOf(1000));

        return accountRepo.save(account);
    }
}
