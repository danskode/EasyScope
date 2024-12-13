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

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class SubProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;



    @Test
    public void testShowSubProjectList() throws Exception {
        session = new MockHttpSession();
        Account account = new Account();
        account.setAccountID(1);
        account.setAccountType(Account.AccountType.PROJECT_MANAGER);
        session.setAttribute("account", account);

        // Assuming projectID 1 exists in the database
        int projectID = 1;

        mockMvc.perform(get("/projects/subprojects/{projectID}", projectID).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("subprojectList"))
                .andExpect(model().attributeExists("subProjects", "project"));
    }


    @Test
    public void testShowCreateSubProjectPage() throws Exception {
        session = new MockHttpSession();
        Account account = new Account();
        account.setAccountID(1);
        account.setAccountType(Account.AccountType.PROJECT_MANAGER);
        session.setAttribute("account", account);

        // Assuming projectID 1 exists in the database
        int projectID = 1;

        mockMvc.perform(get("/projects/subprojects/create/{projectID}", projectID).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("createSubproject"))
                .andExpect(model().attributeExists("project", "subProject"));
    }


    @Test
    public void testShowUpdateSubProjectPage() throws Exception {
        session = new MockHttpSession();
        Account account = new Account();
        account.setAccountID(1);
        account.setAccountType(Account.AccountType.PROJECT_MANAGER);
        session.setAttribute("account", account);

        // Assuming subProjectID 1 exists in the database
        int subProjectID = 1;

        mockMvc.perform(get("/projects/subprojects/update/{subProjectID}", subProjectID).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("updateSubproject"))
                .andExpect(model().attributeExists("subProject"));
    }

}