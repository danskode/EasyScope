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

    // bliver ikke brugt?
    public Account getAccountFromID(int id) {
        return  accountRepository.getAccountFromID(id);
    }

    public void updateAccount(Account account, String newAccountName) {
        accountRepository.updateAccount(account, newAccountName);
    }

    public void updateAccountType(int accountID, Account.AccountType newAccountType) {
        accountRepository.updateAccountType(accountID, newAccountType);
    }

    public void addAccount(String accountName, String accountPassword, Account.AccountType accountType) {
        accountRepository.addAccount(accountName, accountPassword, accountType);
    }

    public List <Account> getAllTeamMembers(){
        return accountRepository.getAllTeamMembers();
    }

    // skal måske ikke bruges alligevel
    public Account getTeamMemberByTaskID(int taskID) {
        return accountRepository.getTeamMemberByTaskID(taskID);
    }
}
