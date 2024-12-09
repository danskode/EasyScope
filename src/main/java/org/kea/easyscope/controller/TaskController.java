package org.kea.easyscope.controller;

import jakarta.servlet.http.HttpSession;
import org.kea.easyscope.model.Account;
import org.kea.easyscope.model.SubProject;
import org.kea.easyscope.model.Task;
import org.kea.easyscope.repository.AccountRepository;
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
    private final AccountRepository accountRepository;

    public TaskController(TaskService taskService, SubProjectService subProjectService, AccountRepository accountRepository) {
        this.taskService = taskService;
        this.subProjectService = subProjectService;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/{projectID}/{subProjectID}")
    public String showTasksList(@PathVariable int projectID,
                                @PathVariable int subProjectID,
                                Model model) {
        // Retrieve the subproject based on subProjectID
        SubProject subProject = subProjectService.getSubProjectBySubProjectID(subProjectID);

        if (subProject != null) {
            // Fetch tasks associated with the subProjectID
            List<Task> tasks = taskService.getTasksBySubProjectID(subProjectID);

            // Add tasks and subProject to the model
            model.addAttribute("tasks", tasks);
            model.addAttribute("subProject", subProject);

            // Add projectID to the model for the back button functionality
            model.addAttribute("projectID", projectID);

            // Return the taskList view
            return "taskList";
        } else {
            // Redirect to the subproject list if subProject is not found
            return "redirect:/subprojectList";
        }
    }

    @GetMapping("/create/{projectID}/{subProjectID}")
    public String showCreateTaskForm(@PathVariable int projectID,
                                     @PathVariable int subProjectID,
                                     HttpSession session,
                                     Model model) {

        // Tilføj kun de nødvendige data til modellen
        model.addAttribute("projectID", projectID);
        model.addAttribute("subProjectID", subProjectID);

        // Hent teammedlemmer og tilføj dem til modellen
        List<Account> teamMembers = accountRepository.getAllTeamMembers();

        model.addAttribute("teamMembers", teamMembers);

        return "createTask"; // Returnerer view, der skal vise formularen
    }

    @PostMapping("/create")
    public String createTask(@RequestParam int projectID,
                             @RequestParam int subProjectID,
                             @RequestParam int accountID,
                             @RequestParam String taskName,
                             @RequestParam String taskDescription,
                             @RequestParam float estimatedHours) {
        // Opret task objekt
        Task task = new Task();
        task.setTaskName(taskName);
        task.setTaskDescription(taskDescription);
        task.setSubProjectID(subProjectID);

        // Brug repository til at oprette task og tildele medlemmet
        taskService.createNewTask(task, accountID, estimatedHours);

        // Redirect tilbage til tasks-listen
        return "redirect:/projects/subprojects/tasks/" + projectID + "/" + subProjectID;
    }

    @GetMapping("/update/{taskID}")
    public String showUpdateTaskPage(@PathVariable int taskID, Model model) {
        // Hent tasken fra databasen baseret på taskID
        Task task = taskService.getTaskByID(taskID);

        // Hent alle teammedlemmer, hvis de skal vises i formularen
        List<Account> teamMembers = accountRepository.getAllTeamMembers();

        // Tilføj task og teamMembers til model, så de kan bruges i formularen
        model.addAttribute("task", task);
        model.addAttribute("teamMembers", teamMembers);

        return "updateTask";
    }

    @PostMapping("/update/{taskID}")
    public String updateTask(@PathVariable int taskID,
                             @ModelAttribute Task task,
                             @RequestParam int memberID,
                             @RequestParam float estimatedHours) {
        task.setTaskID(taskID);  // sørger for at taskID bliver sat korrekt
        taskService.updateTask(task, memberID, estimatedHours);
        return "redirect:/projects/subprojects/tasks/" + taskID;
    }


}
