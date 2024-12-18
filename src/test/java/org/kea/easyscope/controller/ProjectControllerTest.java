package org.kea.easyscope.controller;

import org.junit.jupiter.api.Test;
import org.kea.easyscope.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// annotates the test as a Spring Boot test, using the test profile
@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc // configures mockMvc, which is used to simulate HTTP requests
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc; // mockMvc is used to simulate web requests

    private MockHttpSession session; // session, which we need to mock the user's session

    // test to verify that the "create project" page is displayed correctly
    @Test
    public void testShowCreateProjectPage() throws Exception {
        // arrange: create a new session and an account
        session = new MockHttpSession();
        Account account = new Account();
        account.setAccountID(1);// set account id to 1
        account.setAccountType(Account.AccountType.PROJECT_MANAGER);// set account type to project manager
        session.setAttribute("account", account);// store the account in the session

        // act: perform a GET request to /projects/create with the session
        mockMvc.perform(get("/projects/create").session(session))
                .andExpect(status().isOk()) // expect a 200 OK status
                .andExpect(view().name("createProject")) // expect the view name to be "createProject"
                .andExpect(model().attributeExists("account")); // expect that "account" exists in the model
    }

    // test to verify that creating a project works correctly
    @Test
    public void testCreateProject() throws Exception {
        // arrange: create a new session and an account
        session = new MockHttpSession();
        Account account = new Account();
        account.setAccountID(1); // set account id to 1
        account.setAccountType(Account.AccountType.PROJECT_MANAGER); // set account type to project manager
        session.setAttribute("account", account); // store the account in the session

        // act: perform a POST request to /projects/create with form parameters and the session
        mockMvc.perform(post("/projects/create")
                        .param("projectName", "Test Project") // send the project name as a parameter
                        .param("option", "manual") // send "manual" as the option
                        .param("manualProjectDescription", "Test description") // send the project description
                        .session(session) // include the session in the request
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)) // set content-type to form-data
                .andExpect(status().is3xxRedirection()) // expect a 3xx (redirect) status code
                .andExpect(redirectedUrl("/projects/list")); // expect that the user is redirected to /projects/list
    }
}