package org.kea.easyscope.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kea.easyscope.model.Account;
import org.kea.easyscope.model.Project;
import org.kea.easyscope.model.SubProject;
import org.kea.easyscope.model.Task;
import org.kea.easyscope.service.AccountService;
import org.kea.easyscope.service.ProjectService;
import org.kea.easyscope.service.SubProjectService;
import org.kea.easyscope.service.TaskService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.Collections;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)  // Enables Mockito extension for JUnit 5
public class TaskControllerTest {

    // Mock dependencies for the TaskController class
    @InjectMocks
    private TaskController taskController; // This creates an instance of TaskController and injects mocks into it

    @Mock
    private TaskService taskService;        // Mock the TaskService dependency
    @Mock
    private SubProjectService subProjectService; // Mock the SubProjectService dependency
    @Mock
    private AccountService accountService;  // Mock the AccountService dependency
    @Mock
    private ProjectService projectService;  // Mock the ProjectService dependency
    @Mock
    private Model model;                    // Mock the Model interface used for passing data to the view
    @Mock
    private HttpSession session;            // Mock the HttpSession used in the methods

    private Account account;                // A mock account object
    private Project project;                // A mock project object
    private SubProject subProject;          // A mock subproject object
    private Task task;                      // A mock task object

    @BeforeEach
    void setUp() {
        // Initialize mock objects with default values before each test
        account = new Account();
        project = new Project();
        subProject = new SubProject();
        task = new Task();
        project.setProjectID(1);             // Set a project ID
        subProject.setSubProjectID(1);       // Set a subproject ID
        task.setTaskID(1);                   // Set a task ID

        // Mock the session to return the account object when getAttribute("account") is called
        when(session.getAttribute("account")).thenReturn(account);
    }

    @Test
    void testShowTasksList_WhenAccountIsNull_ShouldThrowException() {
        // arrange: mock session to return null for account
        when(session.getAttribute("account")).thenReturn(null);

        // act & assert: expect an exception to be thrown when calling the method
        assertThrows(IllegalArgumentException.class, () -> taskController.showTasksList(1, 1, session, model));
        // The lambda expression '() -> taskController.showTasksList(1, 1, session, model)' is expected to throw an exception
    }

    @Test
    void testShowTasksList_WhenSubProjectIsNull_ShouldThrowException() {
        // arrange: mock session and subproject service to return null for subproject
        when(session.getAttribute("account")).thenReturn(account);
        when(subProjectService.getSubProjectBySubProjectID(1)).thenReturn(null);

        // act & assert: expect an exception to be thrown when calling the method
        assertThrows(IllegalArgumentException.class, () -> taskController.showTasksList(1, 1, session, model));
    }

    @Test
    void testShowTasksList_WhenProjectIsNull_ShouldThrowException() {
        // arrange: mock session, subproject and project service to return null for project
        when(session.getAttribute("account")).thenReturn(account);
        when(subProjectService.getSubProjectBySubProjectID(1)).thenReturn(subProject);
        when(projectService.getProjectByProjectID(1)).thenReturn(null);

        // act & assert: expect an exception to be thrown when calling the method
        assertThrows(IllegalArgumentException.class, () -> taskController.showTasksList(1, 1, session, model));
    }

    @Test
    void testShowTasksList_ShouldReturnTaskListView() {
        // arrange: mock session, subproject, task service, and project service to return valid data
        when(session.getAttribute("account")).thenReturn(account);
        when(subProjectService.getSubProjectBySubProjectID(1)).thenReturn(subProject);
        when(taskService.getTasksBySubProjectID(1)).thenReturn(Collections.singletonList(task));
        when(projectService.getProjectByProjectID(1)).thenReturn(project);

        // act: call the method to get the view name
        String viewName = taskController.showTasksList(1, 1, session, model);

        // assert: check that the returned view name is correct and verify the model attributes
        assertEquals("taskList", viewName);
        // Verifying that the model attributes are correctly added
        verify(model).addAttribute("tasks", Collections.singletonList(task));
        verify(model).addAttribute("subProject", subProject);
        verify(model).addAttribute("projectID", 1);
        verify(model).addAttribute("projectName", project.getProjectName());
    }

    @Test
    void testShowCreateTaskForm_WhenAccountIsNull_ShouldThrowException() {
        // arrange: mock session to return null for account
        when(session.getAttribute("account")).thenReturn(null);

        // act & assert: expect an exception to be thrown when calling the method
        assertThrows(IllegalArgumentException.class, () -> taskController.showCreateTaskForm(1, 1, session, model));
    }

    @Test
    void testCreateTask_WhenAccountIsNull_ShouldThrowException() {
        // arrange: mock session to return null for account
        when(session.getAttribute("account")).thenReturn(null);

        // act & assert: expect an exception to be thrown when calling the method
        assertThrows(IllegalArgumentException.class, () -> taskController.createTask(1, 1, 1, "task", "description", 5,  LocalDate.of(2025, 12, 24), model, session));
    }

    @Test
    void testShowUpdateTaskPage_WhenAccountIsNull_ShouldThrowException() {
        // arrange: mock session to return null for account
        when(session.getAttribute("account")).thenReturn(null);

        // act & assert: expect an exception to be thrown when calling the method
        assertThrows(IllegalArgumentException.class, () -> taskController.showUpdateTaskPage(1, model, session));
    }

    @Test
    void testUpdateTask_WhenAccountIsNull_ShouldThrowException() {
        // arrange: mock session to return null for account
        when(session.getAttribute("account")).thenReturn(null);

        // act & assert: expect an exception to be thrown when calling the method
        assertThrows(IllegalArgumentException.class, () -> taskController.updateTask(1, 1, 5.0F, 1, 1, task, session, model));
    }
}