package org.kea.easyscope.controller;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kea.easyscope.model.Account;
import org.kea.easyscope.service.CalcService;
import org.kea.easyscope.service.ProjectService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ui.Model;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// unit test
class ProjectControllerTest {

    private ProjectController underTest;
    private HttpSession mockSession;
    private Model mockModel;

    @BeforeEach
    void setUp() {
        // mock dependencies
        ProjectService projectService = mock(ProjectService.class);
        ChatClient chatClient = mock(ChatClient.class);
        CalcService calcService = mock(CalcService.class);

        // inject mocks into the controller
        underTest = new ProjectController(projectService, chatClient, calcService);

        // mock session and model
        mockSession = mock(HttpSession.class);
        mockModel = mock(Model.class);
    }

    @Test
    void showCreateProjectPage_withValidProjectManagerAccount_shouldReturnCreateProject() {
        // arrange: create a mock project manager account
        Account mockAccount = new Account(1, "projectManager", "password", Account.AccountType.PROJECT_MANAGER);
        when(mockSession.getAttribute("account")).thenReturn(mockAccount);

        // act: call the method to show the create project page
        String viewName = underTest.showCreateProjectPage(mockSession, mockModel);

        // assert: verify the view name and that the account is added to the model
        assertEquals("createProject", viewName);
        verify(mockModel).addAttribute("account", mockAccount); // verify account added to model
    }

    @Test
    void showCreateProjectPage_withValidAdminAccount_shouldReturnCreateProject() {
        // arrange: create a mock admin account
        Account mockAccount = new Account(2, "admin", "password", Account.AccountType.ADMIN);
        when(mockSession.getAttribute("account")).thenReturn(mockAccount);

        // act: call the method to show the create project page
        String viewName = underTest.showCreateProjectPage(mockSession, mockModel);

        // assert: verify the view name and that the account is added to the model
        assertEquals("createProject", viewName);
        verify(mockModel).addAttribute("account", mockAccount); // verify account added to model
    }

    @Test
    void showCreateProjectPage_withNoAccountInSession_shouldThrowException() {
        // arrange: simulate no account in session
        when(mockSession.getAttribute("account")).thenReturn(null);

        // act & assert: verify that an exception is thrown when no account is found in session
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> underTest.showCreateProjectPage(mockSession, mockModel));
        assertEquals("No account found in session. Please log in.", exception.getMessage());
    }

    @Test
    void showCreateProjectPage_withUnauthorizedAccount_shouldThrowException() {
        // arrange: create a mock unauthorized account (team member)
        Account mockAccount = new Account(3, "unauthorizedUser", "password", Account.AccountType.TEAM_MEMBER);
        when(mockSession.getAttribute("account")).thenReturn(mockAccount);

        // act & assert: verify that an exception is thrown for unauthorized account
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> underTest.showCreateProjectPage(mockSession, mockModel));
        assertEquals("You don't have permission to create a project.", exception.getMessage());
    }
}