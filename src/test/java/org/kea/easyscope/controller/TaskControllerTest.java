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
import java.util.Collections;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;
    @Mock
    private SubProjectService subProjectService;
    @Mock
    private AccountService accountService;
    @Mock
    private ProjectService projectService;
    @Mock
    private Model model;
    @Mock
    private HttpSession session;

    private Account account;
    private Project project;
    private SubProject subProject;
    private Task task;

    @BeforeEach
    void setUp() {
        account = new Account();
        project = new Project();
        subProject = new SubProject();
        task = new Task();
        project.setProjectID(1);
        subProject.setSubProjectID(1);
        task.setTaskID(1);

        // setup session mock to return account
        when(session.getAttribute("account")).thenReturn(account);
    }

    @Test
    void testShowTasksList_WhenAccountIsNull_ShouldThrowException() {
        // arrange: mock session to return null for account
        when(session.getAttribute("account")).thenReturn(null);

        // act & assert: expect an exception to be thrown when calling the method
        assertThrows(IllegalArgumentException.class, () -> taskController.showTasksList(1, 1, session, model));
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
    void testShowCreateTaskForm_ShouldReturnCreateTaskView() {
        // arrange: mock session, project service, and account service to return valid data
        when(session.getAttribute("account")).thenReturn(account);
        when(projectService.getProjectByProjectID(1)).thenReturn(project);
        when(accountService.getAllTeamMembers()).thenReturn(Collections.singletonList(account));

        // act: call the method to get the view name
        String viewName = taskController.showCreateTaskForm(1, 1, session, model);

        // assert: check that the returned view name is correct and verify the model attributes
        assertEquals("createTask", viewName);
        verify(model).addAttribute("projectID", 1);
        verify(model).addAttribute("projectName", project.getProjectName());
        verify(model).addAttribute("subProjectID", 1);
        verify(model).addAttribute("teamMembers", Collections.singletonList(account));
    }

    @Test
    void testCreateTask_WhenAccountIsNull_ShouldThrowException() {
        // arrange: mock session to return null for account
        when(session.getAttribute("account")).thenReturn(null);

        // act & assert: expect an exception to be thrown when calling the method
        assertThrows(IllegalArgumentException.class, () -> taskController.createTask(1, 1, 1, "task", "description", 5, session));
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
        assertThrows(IllegalArgumentException.class, () -> taskController.updateTask(1, 1, 5, 1, 1, task, session));
    }

    @Test
    void testUpdateTask_ShouldRedirectToTaskList() {
        // arrange: mock session and task service to return valid task data
        when(session.getAttribute("account")).thenReturn(account);
        when(taskService.updateTask(any(), anyInt(), anyFloat())).thenReturn(task);

        // act: call the method to get the view name
        String viewName = taskController.updateTask(1, 1, 5, 1, 1, task, session);

        // assert: check that the returned view name is correct (redirect to task list)
        assertEquals("redirect:/projects/subprojects/tasks/1/1", viewName);
    }
}