package org.kea.easyscope.repository;

import org.junit.jupiter.api.Test;
import org.kea.easyscope.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active=test")
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void getAccountFromID() {
        // Arrange: Set up a test account in your database
        int testId = 1;
        // Act: Retrieve the account by ID
        Account account = accountRepository.getAccountFromID(testId);
        // Assert: Verify the result
        assertEquals("Anna", account.getAccountName(), "Account should not be null for a valid ID");
    }
}