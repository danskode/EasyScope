package org.kea.easyscope.controller;

import jakarta.servlet.http.HttpSession;
import org.kea.easyscope.model.Account;
import org.kea.easyscope.model.Project;
import org.kea.easyscope.model.SubProject;
import org.kea.easyscope.service.ProjectService;
import org.kea.easyscope.service.SubProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/projects/subprojects")
public class SubProjectController {

    private final SubProjectService subProjectService;
    private final ProjectService projectService;

    public SubProjectController(SubProjectService subProjectService, ProjectService projectService) {
        this.subProjectService = subProjectService;
        this.projectService = projectService;
    }


    // a GET method that shows a list of sub projects for a specific project
    @GetMapping("/{projectID}")
    public String showSubProjectList(@PathVariable int projectID, Model model) {
        // retrieve a project based on project ID
        Project project = projectService.getProjectByProjectID(projectID);

        // if project is found
        if (project != null) {
            List<SubProject> subProjects = subProjectService.getSubProjectsByProjectID(projectID);

            // send subProjects and project to the formular
            model.addAttribute("subProjects", subProjects);
            model.addAttribute("project", project);
            return "subprojectList";
        } else {
            return "redirect:/projectList";
        }
    }

    // a GET method that shows the page for creating a sub project
    @GetMapping("/create/{projectID}")
    public String showCreateSubProjectPage(@PathVariable int projectID, Model model) {
        Project project = projectService.getProjectByProjectID(projectID);

        if (project == null) {
            model.addAttribute("error", "Project not found.");
            return "redirect:/projectList";
        }

        model.addAttribute("project", project);

        model.addAttribute("subProject", new SubProject());
        return "createSubproject";
    }


    // a POST method to create a subproject
    @PostMapping("/create")
    public String createSubProject(@ModelAttribute SubProject subproject,
                                   @RequestParam int projectID,
                                   Model model) {
        Project project = projectService.getProjectByProjectID(projectID);

        if (project != null) {
            subproject.setProjectID(projectID);
            subProjectService.createNewSubProject(subproject);

            List<SubProject> subProjects = subProjectService.getSubProjectsByProjectID(projectID);

            model.addAttribute("subProjects", subProjects);
            model.addAttribute("project", project);

            return "redirect:/projects/subprojects/" + projectID;
        } else {
            return "redirect:/projectList";
        }
    }

    @GetMapping("/update/{subProjectID}")
    public String showUpdateSubProjectPage(@PathVariable int subProjectID,
                                           Model model) {
        SubProject subProject = subProjectService.getSubProjectBySubProjectID(subProjectID);

        if (subProject == null) {
            model.addAttribute("error", "Subproject not found.");
            return "redirect:/projectList";
        }

        model.addAttribute("subProject", subProject);
        return "updateSubproject"; // Navnet på din HTML-side
    }

    @PostMapping("/update")
    public String updateSubProject(@RequestParam int subProjectID,
                                   @RequestParam String subProjectName,
                                   @RequestParam String subProjectDescription,
                                   @RequestParam LocalDate subProjectDeadline,
                                   @RequestParam(value = "isFinished", defaultValue = "false") boolean isFinished,
                                   HttpSession session,
                                   Model model) {
        // retrieve the logged-in account
        Account account = (Account) session.getAttribute("account");

        // Check if the user is logged in
        if (account == null) {
            model.addAttribute("error", "Please log in to update a subproject.");
            return "redirect:/login";
        }

        // Check if the user has the right permissions -
        // this is just to make sure that teamMembers don't have access to this option
        if (account.getAccountType() != Account.AccountType.ADMIN &&
                account.getAccountType() != Account.AccountType.PROJECT_MANAGER) {
            model.addAttribute("error", "You don't have permission to update subprojects.");
            return "redirect:/projects/list";
        }

        // retrieve the existing subproject from the database
        SubProject existingSubProject = subProjectService.getSubProjectBySubProjectID(subProjectID);

        if (existingSubProject == null) {
            model.addAttribute("error", "Subproject not found.");
            return "redirect:/projects/list";
        }

        // Update subproject attributes
        existingSubProject.setSubProjectName(subProjectName);
        existingSubProject.setSubProjectDescription(subProjectDescription);
        existingSubProject.setSubProjectDeadline(subProjectDeadline);
        existingSubProject.setFinished(isFinished);

        // Save the updated subproject back to the database
        subProjectService.updateSubProject(existingSubProject);

        // session.setAttribute("updatedSubProject", existingSubProject);

        // This means that you will be redirected to this url /projects/subprojects/{projectID}.
        return "redirect:/projects/subprojects/" + existingSubProject.getProjectID();
    }
}