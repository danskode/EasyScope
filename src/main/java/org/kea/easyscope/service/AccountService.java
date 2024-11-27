package org.kea.easyscope.service;

import org.kea.easyscope.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String getAccountFromID(int id) {
        return  accountRepository.getAccountFromID(id);
    }
}
