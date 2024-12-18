package org.kea.easyscope.repository;

import org.junit.jupiter.api.Test;
import org.kea.easyscope.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        System.out.println("Retrieved account name: " + account.getAccountName());

        // Assert: Verify the result
        assertEquals("Anna", account.getAccountName());
    }

    @Test
    void getAllProjectManagers() {
        // test ID to exclude
        int testId = 1;
        // Act: Call the method
        List<Account> projectManagers = accountRepository.getAllProjectManagers(testId);

        // Assert: Check if the result contains the expected number of project managers
        assertFalse(projectManagers.isEmpty(), "The list of project managers should not be empty");

        // should print out Project Manager: Jens
        projectManagers.forEach(pm -> System.out.println("Project Manager: " + pm.getAccountName()));
    }
}