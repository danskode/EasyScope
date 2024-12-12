package org.kea.easyscope.controller;

import jakarta.servlet.http.HttpSession;
import org.kea.easyscope.model.Account;
import org.kea.easyscope.model.Project;
import org.kea.easyscope.model.SubProject;
import org.kea.easyscope.model.Task;
import org.kea.easyscope.service.AccountService;
import org.kea.easyscope.service.ProjectService;
import org.kea.easyscope.service.SubProjectService;
import org.kea.easyscope.service.TaskService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects/subprojects/tasks")
public class TaskController {

    private final TaskService taskService;
    private final SubProjectService subProjectService;
    private final AccountService accountService;
    private final ChatClient chatClient;
    private final ProjectService projectService;

    // constructor for dependency injection
    public TaskController(TaskService taskService, SubProjectService subProjectService, AccountService accountService, ChatClient chatClient, ProjectService projectService) {
        this.taskService = taskService;
        this.subProjectService = subProjectService;
        this.accountService = accountService;
        this.chatClient = chatClient;
        this.projectService = projectService;
    }

    // method to display the list of tasks for a given project and subproject
    @GetMapping("/{projectID}/{subProjectID}")
    public String showTasksList(@PathVariable int projectID,
                                @PathVariable int subProjectID,
                                HttpSession session,
                                Model model) {
        // Retrieve the account from the ongoing session
        Account account = (Account) session.getAttribute("account");

        // Check if the account exists in session
        if (account == null) {
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }

        // Retrieve the subproject based on subProjectID
        SubProject subProject = subProjectService.getSubProjectBySubProjectID(subProjectID);

        if (subProject == null) {
            throw new IllegalArgumentException("Subproject not found.");
        }

        // Fetch tasks associated with the subProjectID
        List<Task> tasks = taskService.getTasksBySubProjectID(subProjectID);

        // Add tasks and subProject to the model for the view
        model.addAttribute("tasks", tasks);
        model.addAttribute("subProject", subProject);

        // Retrieve project based on projectID
        Project project = projectService.getProjectByProjectID(projectID);

        if (project == null) {
            throw new IllegalArgumentException("Project not found.");
        }

        String projectName = project.getProjectName();

        // Add projectID to the model for the back button functionality
        model.addAttribute("projectID", projectID);
        model.addAttribute("projectName", projectName);

        // Return the task list view
        return "taskList";
    }


    // method to show the form to create a new task
    @GetMapping("/create/{projectID}/{subProjectID}")
    public String showCreateTaskForm(@PathVariable int projectID,
                                     @PathVariable int subProjectID,
                                     HttpSession session,
                                     Model model) {
        // Retrieve the account from the ongoing session
        Account account = (Account) session.getAttribute("account");

        // Check if the account exists in session
        if (account == null) {
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }

        // Retrieve the project based on projectID
        Project project = projectService.getProjectByProjectID(projectID);

        if (project == null) {
            throw new IllegalArgumentException("Project not found.");
        }

        String projectName = project.getProjectName();

        // Add necessary data to the model
        model.addAttribute("projectID", projectID);
        model.addAttribute("projectName", projectName);
        model.addAttribute("subProjectID", subProjectID);

        // Retrieve team members and add them to the model for selection in the form
        List<Account> teamMembers = accountService.getAllTeamMembers();
        model.addAttribute("teamMembers", teamMembers);

        // Return the view that will show the create task form
        return "createTask";
    }

    // method to create a new task after the form is submitted
    @PostMapping("/create")
    public String createTask(@RequestParam int projectID,
                             @RequestParam int subProjectID,
                             @RequestParam int accountID,
                             @RequestParam String taskName,
                             @RequestParam String taskDescription,
                             @RequestParam float estimatedHours,
                             HttpSession session) {

        // Retrieve the account from the ongoing session
        Account account = (Account) session.getAttribute("account");

        // Check if the account exists in session
        if (account == null) {
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }

        Project project = projectService.getProjectByProjectID(projectID);

        if (project == null) {
            throw new IllegalArgumentException("Project not found.");
        }

        // Generate taskDescription using AI
        String aiPrompt =   "This is a task for our client " + project.getProjectName() +": Generate a short numbered list of five good advises " +
                "to the team member that has to complete the task for the client. The task is " + taskName +" and we want to " + taskDescription +
                ". The list must give the team member advice on how to solve the task and comply with ESG and be more efficient. " +
                "Do not include a time estimate. Response must not be longer than 400 characters, including spaces.";

        String aiResponse = this.chatClient.prompt().user(aiPrompt).call().content();

        // Enforce the length limit
        int maxLength = 350;
        if (aiResponse.length() > maxLength) {
            aiResponse = aiResponse.substring(0, maxLength).trim();
        }

        taskDescription = taskDescription + "\n\nSome AI pointers to make task more ESG friendly:\n" + aiResponse;

        // Create a new task object
        Task task = new Task();
        task.setTaskName(taskName);
        task.setTaskDescription(taskDescription);
        task.setSubProjectID(subProjectID);

        // Use the service to create the task and assign the team member
        taskService.createNewTask(task, accountID, estimatedHours);

        // Redirect back to the task list for the given project and subproject
        return "redirect:/projects/subprojects/tasks/" + projectID + "/" + subProjectID;
    }


    // method to show the page where the task can be updated
    @GetMapping("/update/{taskID}")
    public String showUpdateTaskPage(@PathVariable int taskID,
                                     Model model,
                                     HttpSession session) {
        // Retrieve the account from the ongoing session
        Account account = (Account) session.getAttribute("account");

        // Check if the account exists in session
        if (account == null) {
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }

        // Fetch the task from the database based on taskID
        Task task = taskService.getTaskByID(taskID);

        if (task == null) {
            throw new IllegalArgumentException("Task not found.");
        }

        // Retrieve the subproject related to the task
        SubProject subProject = subProjectService.getSubProjectBySubProjectID(task.getSubProjectID());

        if (subProject == null) {
            throw new IllegalArgumentException("Subproject not found.");
        }

        // Fetch all team members, if they need to be displayed in the update form
        List<Account> teamMembers = accountService.getAllTeamMembers();

        // Add the task, team members, and subproject to the model for the update form
        model.addAttribute("task", task);
        model.addAttribute("teamMembers", teamMembers);
        model.addAttribute("subProject", subProject);

        // Return the update task page
        return "updateTask";
    }

    // method to update the task after the form is submitted
    @PostMapping("/update")
    public String updateTask(@RequestParam int taskID,
                             @RequestParam int memberID,
                             @RequestParam float estimatedHours,
                             @RequestParam int projectID,
                             @RequestParam int subProjectID,
                             @ModelAttribute Task task,
                             HttpSession session) {
        // Retrieve the account from the ongoing session
        Account account = (Account) session.getAttribute("account");

        // Check if the account exists in session
        if (account == null) {
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }

        // Set the taskID to the task object
        task.setTaskID(taskID);

        // Update the task using the service method
        taskService.updateTask(task, memberID, estimatedHours);

        // Redirect back to the task list for the given project and subproject
        return "redirect:/projects/subprojects/tasks/" + projectID + "/" + subProjectID;
    }

    @GetMapping("/assigned")
    public String showAssignedTasks(@RequestParam int accountID,
                                    Model model,
                                    HttpSession session) {
        // Retrieve the account from the ongoing session
        Account account = (Account) session.getAttribute("account");

        // Check if the account exists in session
        if (account == null) {
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }

        // Retrieve tasks assigned to the provided accountID
        List<Task> assignedTask = taskService.getTasksAssignedTo(accountID);

        // Add the tasks and accountID to the model for the view
        model.addAttribute("assignedTask", assignedTask);
        model.addAttribute("accountID", accountID);

        // Return the view that displays the list of assigned tasks
        return "assignedTaskList";
    }



    @GetMapping("/finish/{taskID}/{accountID}")
    public String showFinishTaskForm(@PathVariable int taskID,
                                     @PathVariable int accountID,
                                     Model model,
                                     HttpSession session) {
        // Retrieve the account from the ongoing session
        Account account = (Account) session.getAttribute("account");

        // Check if the account exists in session
        if (account == null) {
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }

        // Retrieve the task by its taskID
        Task task = taskService.getTaskByID(taskID);

        // Check if the task exists
        if (task == null) {
            throw new IllegalArgumentException("Task not found.");
        }

        // Add the task and account ID to the model
        model.addAttribute("task", task);
        model.addAttribute("accountID", accountID);

        // Return the view to finish the task
        return "finishTask";
    }


    // This method handles the process of finishing a task
    // when a team member submits the realized hours
    @PostMapping("/finish")
    public String finishTask(@RequestParam int taskID,
                             @RequestParam int accountID,
                             @RequestParam boolean taskIsFinished,
                             @RequestParam float realizedHours,
                             Model model,
                             HttpSession session) {

        // Retrieve the account from the ongoing session
        Account account = (Account) session.getAttribute("account");

        // Check if the account exists in session
        if (account == null) {
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }

        // Retrieve the task by its ID
        Task task = taskService.getTaskByID(taskID);

        // Check if the task exists
        if (task == null) {
            throw new IllegalArgumentException("Task not found.");
        }

        // Update the task's status and realized hours
        taskService.finishTask(task, taskIsFinished, realizedHours);

        // Retrieve the updated list of assigned tasks
        List<Task> assignedTasks = taskService.getTasksAssignedTo(accountID);

        // Add the updated list of assigned tasks and account ID to the model
        model.addAttribute("assignedTask", assignedTasks);
        model.addAttribute("accountID", accountID);

        // Return the view with the updated task list
        return "assignedTaskList";
    }


    // list of finished tasks
    @GetMapping("/finished")
    public String showFinishedTasks(@RequestParam int accountID,
                                    Model model,
                                    HttpSession session) {

        // Retrieve the account from the ongoing session
        Account account = (Account) session.getAttribute("account");

        // Check if the account exists in session
        if (account == null) {
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }

        // Retrieve the list of finished tasks
        List<Task> finishedTasks = taskService.getFinishedTasks(accountID);

        // Add account ID and finished tasks to the model
        model.addAttribute("accountID", accountID);
        model.addAttribute("finishedTasks", finishedTasks);

        // Return the view to display finished tasks
        return "assignedFinishedTaskList";
    }


    // method to delete a task
    @PostMapping("/delete")
    public String deleteTask(@RequestParam int taskID,
                             @RequestParam int projectID,
                             @RequestParam int subProjectID,
                             HttpSession session) {

        // Retrieve the account from the ongoing session
        Account account = (Account) session.getAttribute("account");

        // Check if the account exists in session
        if (account == null) {
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }

        // delete the task by its ID using the service
        taskService.deleteTaskByID(taskID);

        // redirect back to the task list for the given project and subproject
        return "redirect:/projects/subprojects/tasks/" + projectID + "/" + subProjectID;
    }

}