package org.kea.easyscope.service;

import org.junit.jupiter.api.Test;
import org.kea.easyscope.model.Account;
import org.kea.easyscope.repository.AccountRepository;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

// this is a unit test
class AccountServiceTest {

    @Test
    void getAccountFromAccountName() {
        // Arrange: Mock AccountRepository
        AccountRepository mockRepo = Mockito.mock(AccountRepository.class);
        String testAccountName = "TestAccount";
        Account mockAccount = new Account(1, testAccountName, "itsasecret", Account.AccountType.ADMIN);

        // Mock repo behavior
        Mockito.when(mockRepo.getAccountFromAccountName(testAccountName)).thenReturn(mockAccount);

        // Create service with mocked repo
        AccountService underTest = new AccountService(mockRepo);

        // Act: Call the service method
        Account actualAccount = underTest.getAccountFromAccountName(testAccountName);

        // Assert: Verify the result
        assertNotNull(actualAccount);
        assertEquals(1, actualAccount.getAccountID());
        assertEquals(testAccountName, actualAccount.getAccountName());
        assertEquals(Account.AccountType.ADMIN, actualAccount.getAccountType());

        // console out print
        System.out.print("This should print TestAccount: " + actualAccount.getAccountName());
        System.out.print("This should print ADMIN: " + actualAccount.getAccountType());

    }
}