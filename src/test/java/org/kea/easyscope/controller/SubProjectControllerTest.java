package org.kea.easyscope.controller;

import org.junit.jupiter.api.Test;
import org.kea.easyscope.model.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// annotates the test as a Spring Boot test, using the test profile
@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc // configures mockMvc to simulate HTTP requests
class SubProjectControllerTest {

    @Autowired
    private MockMvc mockMvc; // mockMvc is used to simulate web requests

    private MockHttpSession session; // session object to mock the user's session

    // test to verify that the subproject list page is displayed correctly
    @Test
    public void testShowSubProjectList() throws Exception {
        // arrange: create a new session and an account for a project manager
        session = new MockHttpSession();
        Account account = new Account();
        account.setAccountID(1); // set account id to 1
        account.setAccountType(Account.AccountType.PROJECT_MANAGER); // set account type to project manager
        session.setAttribute("account", account); // store the account in the session

        // assuming that projectID 1 exists in the database
        int projectID = 1;

        // act: perform a GET request to /projects/subprojects/{projectID}
        mockMvc.perform(get("/projects/subprojects/{projectID}", projectID).session(session))
                // assert: Verifying the HTTP status code is 200 (OK)
                .andExpect(status().isOk())
                // assert: Verifying the view name returned is "subprojectList"
                .andExpect(view().name("subprojectList"))
                // assert: Verifying that "subProjects" and "project" are present in the model
                .andExpect(model().attributeExists("subProjects", "project"));
    }

    // test to verify that the create subproject page is displayed correctly
    @Test
    public void testShowCreateSubProjectPage() throws Exception {
        // arrange: create a new session and an account for a project manager
        session = new MockHttpSession();
        Account account = new Account();
        account.setAccountID(1); // set account id to 1
        account.setAccountType(Account.AccountType.PROJECT_MANAGER); // set account type to project manager
        session.setAttribute("account", account); // store the account in the session

        // assuming that projectID 1 exists in the database
        int projectID = 1;

        // act: perform a GET request to /projects/subprojects/create/{projectID}
        mockMvc.perform(get("/projects/subprojects/create/{projectID}", projectID).session(session))
                // Assert: Verifying the HTTP status code is 200 (OK)
                .andExpect(status().isOk())
                // Assert: Verifying the view name returned is "createSubproject"
                .andExpect(view().name("createSubproject"))
                // Assert: Verifying that "project" and "subProject" are present in the model
                .andExpect(model().attributeExists("project", "subProject"));
    }

    // test to verify that the update subproject page is displayed correctly
    @Test
    public void testShowUpdateSubProjectPage() throws Exception {
        // arrange: create a new session and an account for a project manager
        session = new MockHttpSession();
        Account account = new Account();
        account.setAccountID(1); // set account id to 1
        account.setAccountType(Account.AccountType.PROJECT_MANAGER); // set account type to project manager
        session.setAttribute("account", account); // store the account in the session

        // assuming that subProjectID 1 exists in the database
        int subProjectID = 1;

        // act: perform a GET request to /projects/subprojects/update/{subProjectID}
        mockMvc.perform(get("/projects/subprojects/update/{subProjectID}", subProjectID).session(session))
                // assert: Verifying the HTTP status code is 200 (OK)
                .andExpect(status().isOk())
                // assert: Verifying the view name returned is "updateSubproject"
                .andExpect(view().name("updateSubproject"))
                // assert: Verifying that "subProject" is present in the model
                .andExpect(model().attributeExists("subProject"));
    }

}