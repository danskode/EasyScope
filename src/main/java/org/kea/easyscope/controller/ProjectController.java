package org.kea.easyscope.controller;

import jakarta.servlet.http.HttpSession;
import org.kea.easyscope.model.Account;
import org.kea.easyscope.model.Project;
import org.kea.easyscope.service.CalcService;
import org.kea.easyscope.service.ProjectService;
import org.springframework.ai.chat.client.ChatClient;
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
    private final ChatClient chatClient;
    private final CalcService calcService;

    // constructor to inject the ProjectService dependency
    public ProjectController(ProjectService projectService, ChatClient chatClient, CalcService calcService) {
        this.projectService = projectService;
        this.chatClient = chatClient;
        this.calcService = calcService;
    }

    // a GET method to show the "createProject" html
    @GetMapping("/create")
    public String showCreateProjectPage(HttpSession session,
                                        Model model) {
        // Retrieve the account from the ongoing session
        Account account = (Account) session.getAttribute("account");

        // Check if account exists in session
        if (account == null) {
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }

        // Check if the account has the necessary role (PROJECT_MANAGER or ADMIN)
        if (account.getAccountType() == Account.AccountType.PROJECT_MANAGER ||
                account.getAccountType() == Account.AccountType.ADMIN) {
            // Add account to the model for use in the view
            model.addAttribute("account", account);
            return "createProject";
        } else {
            // If the account is not authorized to create a project, throw an exception
            throw new IllegalArgumentException("You don't have permission to create a project.");
        }
    }
//    @GetMapping("/create")
//    public String showCreateProjectPage(HttpSession session,
//                                        Model model) {
//        // get the account from the ongoing session
//        Account account = (Account) session.getAttribute("account");
//
//        if (account != null) {
//            // check if the account type is either PROJECT_MANAGER or ADMIN
//            if (account.getAccountType() == Account.AccountType.PROJECT_MANAGER ||
//                    account.getAccountType() == Account.AccountType.ADMIN) {
//                // add account to the model for use in the view
//                model.addAttribute("account", account);
//                return "createProject";
//            } else {
//                // If the account is a TEAM_MEMBER, redirect to a different page
//                model.addAttribute("error", "You don't have permission to create a project.");
//                // redirect to project list
//                return "redirect:/projects/list";
//            }
//        } else {
//            System.out.println("No account found in session! Redirecting to login.");
//            return "redirect:/login";
//        }
//    }

    @PostMapping("/create")
    public String createProject(
            @RequestParam String projectName,
            @RequestParam String option, // Option: "ai" or "manual"
            @RequestParam(required = false) String aiProjectDescription, // From AI
            @RequestParam(required = false) String manualProjectDescription, // From user
            HttpSession session) {

        // Retrieve the account from the session
        Account account = (Account) session.getAttribute("account");

        // Check if the account is null (no account found in session)
        if (account == null) {
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }

        // Check if the account type is either PROJECT_MANAGER or ADMIN
        if (account.getAccountType() != Account.AccountType.PROJECT_MANAGER &&
                account.getAccountType() != Account.AccountType.ADMIN) {
            throw new IllegalArgumentException("You don't have permission to create a project.");
        }

        // Initialize the project description based on the selected option
        String projectDescription = "";
        if ("ai".equals(option)) {
            // Generate project description using AI
            String aiPrompt = "This is a project: Generate a concise description of our client: " + projectName +
                    ". Include knowledge about the company's specific stand on environment and UN goals if available. " +
                    "Do not include a time estimate.";
            String aiResponse = this.chatClient.prompt().user(aiPrompt).call().content();

            // Check if AI response is valid
            if (aiResponse == null || aiResponse.isEmpty()) {
                throw new IllegalArgumentException("AI failed to generate a project description. Please try again.");
            }

            // Enforce the length limit for the AI description
            int maxLength = 350;
            if (aiResponse.length() > maxLength) {
                aiResponse = aiResponse.substring(0, maxLength).trim();
            }
            projectDescription = aiResponse;

        } else if ("manual".equals(option)) {
            projectDescription = manualProjectDescription;
        } else {
            // Throw an exception if option is neither "ai" nor "manual"
            throw new IllegalArgumentException("Invalid option selected. Please choose 'ai' or 'manual'.");
        }

        // Create a new Project object
        Project newProject = new Project();
        newProject.setProjectName(projectName);
        newProject.setProjectDescription(projectDescription);
        newProject.setFinished(false); // Set project to active
        newProject.setAccountID(account.getAccountID()); // Associate with the current account

        // Call the service to create the new project in the database
        projectService.createNewProject(newProject);

        // Redirect to the list of projects after successful creation
        return "redirect:/projects/list";
    }

//    @PostMapping("/create")
//    public String createProject(
//            @RequestParam String projectName,
//            @RequestParam String option, // Option: "ai" or "manual"
//            @RequestParam(required = false) String aiProjectDescription, // From AI
//            @RequestParam(required = false) String manualProjectDescription, // From user
//            HttpSession session,
//            Model model) {
//
//        // Retrieve the account from the session
//        Account account = (Account) session.getAttribute("account");
//
//        if (account != null) {
//            // Check if the account type is either PROJECT_MANAGER or ADMIN
//            if (account.getAccountType() == Account.AccountType.PROJECT_MANAGER ||
//                    account.getAccountType() == Account.AccountType.ADMIN) {
//
//                // Initialize the project description based on the selected option
//                String projectDescription = "";
//                if ("ai".equals(option)) {
//                    // Generate project description using AI
//                    String aiPrompt =   "This is a project: Generate a concise description of our client: " + projectName +
//                                        ". Include knowledge about the company's specific stand on environment and UN goals if available. " +
//                                        "Do not include a time estimate.";
//
//                    String aiResponse = this.chatClient.prompt().user(aiPrompt).call().content();
//
//                    // Enforce the length limit
//                    int maxLength = 350;
//                    if (aiResponse.length() > maxLength) {
//                        // Truncate the response to fit the maximum allowed length
//                        aiResponse = aiResponse.substring(0, maxLength).trim();
//                    }
//
//                    projectDescription = aiResponse;
//
//                } else if ("manual".equals(option)) {
//                    // Use the manually provided description
//                    projectDescription = manualProjectDescription;
//                }
//
//                // Create a new Project object with the form data and account information
//                Project newProject = new Project();
//                newProject.setProjectName(projectName);
//                newProject.setProjectDescription(projectDescription);
//                newProject.setFinished(false); // Set project to active
//                newProject.setAccountID(account.getAccountID()); // Associate with the current account
//
//                // Call the service to create the new project in the database
//                projectService.createNewProject(newProject);
//
//                // Redirect to the list of projects after successful creation
//                return "redirect:/projects/list";
//            } else {
//                // If the account is a TEAM_MEMBER, add an error message and redirect to project list
//                model.addAttribute("error", "You don't have permission to create a project.");
//                return "redirect:/projects/list";
//            }
//        } else {
//            model.addAttribute("error", "Something went wrong...");
//            return "redirect:/login";  // Redirect to the login page if no account is found in session
//        }
//    }

    @GetMapping("/list")
    public String showProjectList(HttpSession session,
                                  Model model) {
        // Get the account from the ongoing session
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            // If no account is found in session, throw an IllegalArgumentException
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }

        CalcService cs = calcService;

        // Get the list of projects associated with the logged-in account
        List<Project> projects = projectService.getProjectFromAccountID(account.getAccountID());

        // Add the list of projects to the model for use in HTML
        model.addAttribute("projects", projects);
        model.addAttribute("cs", cs);

        // Return the HTML where the projects are listed
        return "projectList";
    }


    // a GET method to show the update page
    @GetMapping("/update/{projectID}")
    public String showUpdateProjectForm(@PathVariable int projectID,
                                        HttpSession session,
                                        Model model) {
        // Retrieve the account from the session
        Account account = (Account) session.getAttribute("account");

        // Check if account exists in session
        if (account == null) {
            throw new IllegalArgumentException("User not logged in. Please log in first.");
        }

        // Retrieve the project based on project ID
        Project project = projectService.getProjectByProjectID(projectID);

        // Check if the project exists
        if (project == null) {
            throw new IllegalArgumentException("Project with ID " + projectID + " not found.");
        }

        model.addAttribute("project", project);
        return "updateProject";
    }


    // a POST method to update a project
    @PostMapping("/update")
    public String updateProject(@RequestParam int projectID, // Unique identifier for the project
                                @RequestParam String projectName,
                                @RequestParam String projectDescription,
                                @RequestParam(value = "isFinished", defaultValue = "false") boolean isFinished,
                                HttpSession session,
                                Model model) {
        // retrieve the logged-in account
        Account account = (Account) session.getAttribute("account");

        // Check if account exists in session
        if (account == null) {
            throw new IllegalArgumentException("Please log in to update a project.");
        }

        // Check account permissions (admin or project manager)
        if (account.getAccountType() != Account.AccountType.ADMIN &&
                account.getAccountType() != Account.AccountType.PROJECT_MANAGER) {
            throw new IllegalArgumentException("You don't have permission to update projects.");
        }

        // Retrieve the existing project from the database
        Project existingProject = projectService.getProjectByProjectID(projectID);
        if (existingProject == null) {
            throw new IllegalArgumentException("Project not found for ID: " + projectID);
        }

        // Update project attributes
        existingProject.setProjectName(projectName);
        existingProject.setProjectDescription(projectDescription);
        existingProject.setFinished(isFinished);

        // Save updated project back to the database
        projectService.updateProject(existingProject);

        // Redirect to the list of projects
        return "redirect:/projects/list";
    }
//    @PostMapping("/update")
//    public String updateProject(@RequestParam int projectID, // Unique identifier for the project
//                                @RequestParam String projectName,
//                                @RequestParam String projectDescription,
//                                @RequestParam(value = "isFinished", defaultValue = "false") boolean isFinished,
//                                HttpSession session,
//                                Model model) {
//        // retrieve the logged-in account
//        Account account = (Account) session.getAttribute("account");
//
//        // check which account is logged in
//        if (account == null) {
//            model.addAttribute("error", "Please log in to update a project.");
//            return "redirect:/login";
//        }
//
//        if (account.getAccountType() != Account.AccountType.ADMIN &&
//                account.getAccountType() != Account.AccountType.PROJECT_MANAGER) {
//            model.addAttribute("error", "You don't have permission to update projects.");
//            return "redirect:/projects/list";
//        }
//
//        // retrieves the existing project from the db
//        Project existingProject = projectService.getProjectByProjectID(projectID);
//        if (existingProject == null) {
//            System.out.println("Project not found for ID: " + projectID);
//        } else {
//            System.out.println("Existing project: " + existingProject);
//        }
//
//        if (existingProject == null) {
//            model.addAttribute("error", "Project not found.");
//            return "redirect:/projects/list";
//        }
//
//        // update project attributes
//        existingProject.setProjectName(projectName);
//        existingProject.setProjectDescription(projectDescription);
//        existingProject.setFinished(isFinished);
//
//        // save updated project back to the db
//        projectService.updateProject(existingProject);
//
//        // redirect to list of projects
//        return "redirect:/projects/list";
//    }

}

