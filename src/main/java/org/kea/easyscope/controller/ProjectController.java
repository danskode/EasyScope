package org.kea.easyscope.controller;

import jakarta.servlet.http.HttpSession;
import org.kea.easyscope.model.Account;
import org.kea.easyscope.model.Project;
import org.kea.easyscope.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
// all mappings within this class stats with "/projects"
@RequestMapping("/projects")
public class ProjectController {

    // declares a ProjectService to interact with the Service layer
    private final ProjectService projectService;

    // constructor to inject the ProjectService dependency
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // a GET method to show the "createProject" html
    @GetMapping("/create")
    public String showCreateProjectPage(HttpSession session, Model model) {
        // get the account from the ongoing session
        Account account = (Account) session.getAttribute("account");

        if (account != null) {
            // Check if the account type is either PROJECT_MANAGER or ADMIN
            if (account.getAccountType() == Account.AccountType.PROJECT_MANAGER ||
                    account.getAccountType() == Account.AccountType.ADMIN) {
                model.addAttribute("account", account);  // Add account to the model for use in the view
                return "createProject";  // Allow access to create project page
            } else {
                // If the account is a TEAM_MEMBER, redirect to a different page
                model.addAttribute("error", "You don't have permission to create a project.");
                return "redirect:/projects/list";  // Redirect to project list
            }
        } else {
            System.out.println("No account found in session! Redirecting to login.");
            return "redirect:/login";  // Redirect to login page if no account found
        }
    }

    // a POST method to create a project using the form from above
    @PostMapping("/create")
    public String createProject(@RequestParam String projectName,
                                @RequestParam String projectDescription,
                                HttpSession session,
                                Model model) {
        // Retrieve the account from session
        Account account = (Account) session.getAttribute("account");

        if (account != null) {
            // check if the account type is either PROJECT_MANAGER or ADMIN
            if (account.getAccountType() == Account.AccountType.PROJECT_MANAGER ||
                    account.getAccountType() == Account.AccountType.ADMIN) {
                // create a new Project object with the form data and account information
                Project newProject = new Project();
                newProject.setProjectName(projectName);
                newProject.setProjectDescription(projectDescription);
                // set the accountID to associate this project with the account
                newProject.setAccountID(account.getAccountID());

                // Call the service to create the new project in the database
                projectService.createNewProject(newProject);

                // Redirect to the list of projects after successful creation
                return "redirect:/projects/list";
            } else {
                // If the account is a TEAM_MEMBER, add an error message and redirect to project list
                model.addAttribute("error", "You don't have permission to create a project.");
                return "redirect:/projects/list";  // Redirect to project list
            }
        } else {
            model.addAttribute("error", "Something went wrong...");
            return "redirect:/login";  // Redirect to the login page if no account found in session
        }
    }



    // a GET method to show the list of projects for the project manager
    @GetMapping("/list")
    public String showProjectList(HttpSession session, Model model) {
        // get the account from the ongoing session
        Account account = (Account) session.getAttribute("account");

        if (account != null) {
            // get the list of projects associated with the logged in account
            List<Project> projects = projectService.getProjectFromAccountID(account.getAccountID());
            // add the list of projects to the model for use in html
            model.addAttribute("projects", projects);
            // return the html where the projects are listed
            return "projectList";
        } else {
            // if no account is found, return to login
            return "redirect:/login";
        }
    }

    @GetMapping("/update/{projectID}")
    public String showUpdateProjectForm(@PathVariable int projectID, Model model) {
        // Hent projektet baseret på projekt-ID
        Project project = projectService.getProjectByProjectID(projectID);

        // Hvis projektet ikke findes, send brugeren tilbage til projektlisten
        if (project == null) {
            model.addAttribute("error", "Project not found.");
            return "redirect:/projects/list";
        }

        // Send projektet til formularen
        model.addAttribute("project", project);

        return "updateProject";  // Denne side indeholder formularen
    }

    @PostMapping("/update")
    public String updateProject(@RequestParam int projectID, // Unique identifier for the project
                                @RequestParam String projectName,
                                @RequestParam String projectDescription,
                                @RequestParam(value = "isActive", defaultValue = "false") boolean isActive,
                                HttpSession session,
                                Model model) {
        // Retrieve the logged-in account
        Account account = (Account) session.getAttribute("account");

        // Check if the user is logged in
        if (account == null) {
            model.addAttribute("error", "Please log in to update a project.");
            return "redirect:/login";
        }

        // Check if the account has permission to edit projects
        if (account.getAccountType() != Account.AccountType.ADMIN &&
                account.getAccountType() != Account.AccountType.PROJECT_MANAGER) {
            model.addAttribute("error", "You don't have permission to update projects.");
            return "redirect:/projects/list";
        }

        // Retrieve the existing project from the database
        Project existingProject = projectService.getProjectByProjectID(projectID);

        if (existingProject == null) {
            model.addAttribute("error", "Project not found.");
            return "redirect:/projects/list";
        }

        // Update project attributes
        existingProject.setProjectName(projectName);
        existingProject.setProjectDescription(projectDescription);
        existingProject.setActive(isActive);

        // Save updated project back to the database
        projectService.updateProject(existingProject);

        // Redirect to project list or details page
        return "redirect:/projects/list";
    }


    // mark project as done





}

