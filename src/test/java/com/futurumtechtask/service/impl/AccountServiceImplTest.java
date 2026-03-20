package com.futurumtechtask.service.impl;

import com.futurumtechtask.dto.AccountDTO;
import com.futurumtechtask.entity.Account;
import com.futurumtechtask.repo.AccountRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepo accountRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldReturnCurrentAccount() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user");

        Account account = new Account();
        account.setUsername("user");

        when(accountRepo.findByUsername("user")).thenReturn(Optional.of(account));

        Account result = accountService.getCurrentAccount();

        assertEquals("user", result.getUsername());
        verify(accountRepo).findByUsername("user");
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user");

        when(accountRepo.findByUsername("user")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> accountService.getCurrentAccount());

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void shouldRegisterNewAccount() {
        AccountDTO dto = new AccountDTO();
        dto.setUsername("newUser");
        dto.setPassword("password");

        when(accountRepo.existsByUsername("newUser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        Account savedAccount = new Account();
        savedAccount.setUsername("newUser");

        when(accountRepo.save(any(Account.class))).thenReturn(savedAccount);

        Account result = accountService.register(dto);

        assertEquals("newUser", result.getUsername());
        verify(passwordEncoder).encode("password");
        verify(accountRepo).save(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        AccountDTO dto = new AccountDTO();
        dto.setUsername("existing");
        dto.setPassword("password");

        when(accountRepo.existsByUsername("existing")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> accountService.register(dto));

        assertEquals("Username already exists", ex.getMessage());
        verify(accountRepo, never()).save(any());
    }

    @Test
    void shouldSetInitialBalanceOnRegister() {
        AccountDTO dto = new AccountDTO();
        dto.setUsername("user");
        dto.setPassword("pass");

        when(accountRepo.existsByUsername("user")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

        when(accountRepo.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

        accountService.register(dto);

        Account saved = captor.getValue();

        assertEquals(BigDecimal.valueOf(1000), saved.getEmeraldBalance());
    }
}
