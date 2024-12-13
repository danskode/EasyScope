package org.kea.easyscope.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kea.easyscope.model.Account;
import org.kea.easyscope.model.Project;
import org.kea.easyscope.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;



    private MockHttpSession session;

    @Test
    public void testShowCreateProjectPage() throws Exception {
        session = new MockHttpSession();
        Account account = new Account();
        account.setAccountID(1);
        account.setAccountType(Account.AccountType.PROJECT_MANAGER);
        session.setAttribute("account", account);

        mockMvc.perform(get("/projects/create").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("createProject"))
                .andExpect(model().attributeExists("account"));
    }

    @Test
    public void testCreateProject() throws Exception {
        session = new MockHttpSession();
        Account account = new Account();
        account.setAccountID(1);
        account.setAccountType(Account.AccountType.PROJECT_MANAGER);
        session.setAttribute("account", account);

        mockMvc.perform(post("/projects/create")
                        .param("projectName", "Test Project")
                        .param("option", "manual")
                        .param("manualProjectDescription", "Test description")
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/list"));
    }
}