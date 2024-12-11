package org.kea.easyscope.controller;

import org.kea.easyscope.model.Account;
import org.kea.easyscope.model.SubProject;
import org.kea.easyscope.model.Task;
import org.kea.easyscope.service.AccountService;
import org.kea.easyscope.service.SubProjectService;
import org.kea.easyscope.service.TaskService;
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

    // constructor for dependency injection
    public TaskController(TaskService taskService, SubProjectService subProjectService, AccountService accountService) {
        this.taskService = taskService;
        this.subProjectService = subProjectService;
        this.accountService = accountService;
    }

    // method to display the list of tasks for a given project and subproject
    @GetMapping("/{projectID}/{subProjectID}")
    public String showTasksList(@PathVariable int projectID,
                                @PathVariable int subProjectID,
                                Model model) {
        // retrieve the subproject based on subProjectID
        SubProject subProject = subProjectService.getSubProjectBySubProjectID(subProjectID);


        if (subProject != null) {
            // fetch tasks associated with the subProjectID
            List<Task> tasks = taskService.getTasksBySubProjectID(subProjectID);

            // add tasks and subProject to the model for the view
            model.addAttribute("tasks", tasks);
            model.addAttribute("subProject", subProject);

            // add projectID to the model for the back button functionality
            model.addAttribute("projectID", projectID);

            // return the task list view
            return "taskList";
        } else {
            // redirect to the subproject list if the subProject is not found
            return "redirect:/subprojectList";
        }
    }

    // method to show the form to create a new task
    @GetMapping("/create/{projectID}/{subProjectID}")
    public String showCreateTaskForm(@PathVariable int projectID,
                                     @PathVariable int subProjectID,
                                     Model model) {

        // add necessary data to the model
        model.addAttribute("projectID", projectID);
        model.addAttribute("subProjectID", subProjectID);

        // retrieve team members and add them to the model for selection in the form
        List<Account> teamMembers = accountService.getAllTeamMembers();
        model.addAttribute("teamMembers", teamMembers);

        // return the view that will show the create task form
        return "createTask";
    }

    // method to create a new task after the form is submitted
    @PostMapping("/create")
    public String createTask(@RequestParam int projectID,
                             @RequestParam int subProjectID,
                             @RequestParam int accountID,
                             @RequestParam String taskName,
                             @RequestParam String taskDescription,
                             @RequestParam float estimatedHours) {
        // create a new task object
        Task task = new Task();
        task.setTaskName(taskName);
        task.setTaskDescription(taskDescription);
        task.setSubProjectID(subProjectID);

        // use the service to create the task and assign the team member
        taskService.createNewTask(task, accountID, estimatedHours);

        // redirect back to the task list for the given project and subproject
        return "redirect:/projects/subprojects/tasks/" + projectID + "/" + subProjectID;
    }

    // method to show the page where the task can be updated
    @GetMapping("/update/{taskID}")
    public String showUpdateTaskPage(@PathVariable int taskID, Model model) {
        // fetch the task from the database based on taskID
        Task task = taskService.getTaskByID(taskID);

        // retrieve the subproject related to the task
        SubProject subProject = subProjectService.getSubProjectBySubProjectID(task.getSubProjectID());

        // fetch all team members, if they need to be displayed in the update form
        List<Account> teamMembers = accountService.getAllTeamMembers();

        // add the task, team members, and subproject to the model for the update form
        model.addAttribute("task", task);
        model.addAttribute("teamMembers", teamMembers);
        model.addAttribute("subProject", subProject);

        // return the update task page
        return "updateTask";
    }

    // method to update the task after the form is submitted
    @PostMapping("/update")
    public String updateTask(@RequestParam int taskID,
                             @RequestParam int memberID,
                             @RequestParam float estimatedHours,
                             @RequestParam int projectID,
                             @RequestParam int subProjectID,
                             @ModelAttribute Task task) {
        // set the taskID to the task object
        task.setTaskID(taskID);

        // update the task using the service method
        taskService.updateTask(task, memberID, estimatedHours);

        // redirect back to the task list for the given project and subproject
        return "redirect:/projects/subprojects/tasks/" + projectID + "/" + subProjectID;
    }

    @GetMapping("/assigned")
    public String showAssignedTasks(@RequestParam int accountID,
                                    Model model) {
        List <Task> assignedTask = taskService.getTasksAssignedTo(accountID);
        model.addAttribute("assignedTask", assignedTask);
        model.addAttribute("accountID", accountID);

        return "assignedTaskList";
    }

    @GetMapping("/finish/{taskID}/{accountID}")
    public String showFinishTaskForm(@PathVariable int taskID,
                                     @PathVariable int accountID,
                                     Model model) {
        // retrieve the task by its task ID
        Task task = taskService.getTaskByID(taskID);

        // add the task and account ID to the model
        model.addAttribute("task", task);
        model.addAttribute("accountID", accountID);

        return "finishTask";
    }


    // This method handles the process of finishing a task
    // when a team member submits the realized hours
    @PostMapping("/finish")
    public String finishTask(@RequestParam int taskID,
                             @RequestParam int accountID,
                             @RequestParam boolean taskIsFinished,
                             @RequestParam float realizedHours,
                             Model model) {

        // retrieve the task by its ID
        Task task = taskService.getTaskByID(taskID);

        // update the tasks status and realized hours
        taskService.finishTask(task, taskIsFinished, realizedHours);

        // retrieve the updated list of assigned tasks
        List<Task> assignedTasks = taskService.getTasksAssignedTo(accountID);

        // add the updated list of assigned tasks and account ID to the model
        model.addAttribute("assignedTask", assignedTasks);
        model.addAttribute("accountID", accountID);

        return "assignedTaskList";
    }

    // list of finished tasks
    @GetMapping("/finished")
    public String showFinishedTasks(@RequestParam int accountID,
                                    Model model) {
        List<Task> finishedTasks = taskService.getFinishedTasks(accountID);
        model.addAttribute("accountID", accountID);
        model.addAttribute("finishedTasks", finishedTasks);

        return "assignedFinishedTaskList";
    }




    // method to delete a task
    @PostMapping("/delete")
    public String deleteTask(@RequestParam int taskID,
                             @RequestParam int projectID,
                             @RequestParam int subProjectID) {
        // delete the task by its ID using the service
        taskService.deleteTaskByID(taskID);

        // redirect back to the task list for the given project and subproject
        return "redirect:/projects/subprojects/tasks/" + projectID + "/" + subProjectID;
    }
}