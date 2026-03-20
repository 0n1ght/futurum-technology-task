package com.futurumtechtask.service;

import com.futurumtechtask.dto.AccountDTO;
import com.futurumtechtask.entity.Account;

public interface AccountService {
    Account getCurrentAccount();
    Account register(AccountDTO request);
}
