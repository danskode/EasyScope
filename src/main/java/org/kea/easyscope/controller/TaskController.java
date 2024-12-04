package org.kea.easyscope.controller;

import org.kea.easyscope.model.SubProject;
import org.kea.easyscope.model.Task;
import org.kea.easyscope.service.ProjectService;
import org.kea.easyscope.service.SubProjectService;
import org.kea.easyscope.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/projects/subprojects/tasks")
public class TaskController {

    private final TaskService taskService;
    private final SubProjectService subProjectService;
    private final ProjectService projectService;

    public TaskController(TaskService taskService, SubProjectService subProjectService, ProjectService projectService) {
        this.taskService = taskService;
        this.subProjectService = subProjectService;
        this.projectService = projectService;
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
            System.out.println(tasks);

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


}
