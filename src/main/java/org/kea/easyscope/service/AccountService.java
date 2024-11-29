package org.kea.easyscope.service;

import org.kea.easyscope.model.Account;
import org.kea.easyscope.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountFromAccountName(String accountName){
        return accountRepository.getAccountFromAccountName(accountName);
    }

    public List<Account> getAllNonAdminAccounts(int accountID){
        return accountRepository.getAllNonAdminAccounts(accountID);
    }

    public Account getAccountFromID(int id) {
        return  accountRepository.getAccountFromID(id);
    }

    public void updateAccountType(int accountID, Account.AccountType newAccountType) {
        accountRepository.updateAccountType(accountID, newAccountType);
    }
}
