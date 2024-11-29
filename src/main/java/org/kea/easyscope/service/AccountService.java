package org.kea.easyscope.service;

import org.kea.easyscope.model.Account;
import org.kea.easyscope.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountFromAccountName(String accountName){
        return accountRepository.getAccountFromAccountName(accountName);
    }

    public Account getAccountFromID(int id) {
        return  accountRepository.getAccountFromID(id);
    }
}
