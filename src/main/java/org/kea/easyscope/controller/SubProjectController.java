package org.kea.easyscope.controller;

import jakarta.servlet.http.HttpSession;
import org.kea.easyscope.model.Account;
import org.kea.easyscope.model.Project;
import org.kea.easyscope.model.SubProject;
import org.kea.easyscope.repository.CalcRepository;
import org.kea.easyscope.service.CalcService;
import org.kea.easyscope.service.ProjectService;
import org.kea.easyscope.service.SubProjectService;
import org.springframework.ai.chat.client.ChatClient;
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
    private final CalcService calcService;
    private final ChatClient chatClient;

    public SubProjectController(SubProjectService subProjectService, ProjectService projectService, CalcService calcService, ChatClient chatClient) {
        this.subProjectService = subProjectService;
        this.projectService = projectService;
        this.calcService = calcService;
        this.chatClient = chatClient;
    }


    // a GET method that shows a list of subprojects for a specific project
    @GetMapping("/{projectID}")
    public String showSubProjectList(@PathVariable int projectID,
                                     HttpSession session,
                                     Model model) {
        // Retrieve the account from the ongoing session
        Account account = (Account) session.getAttribute("account");

        // Check if the account exists in session
        if (account == null) {
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }

        // Retrieve the project based on project ID
        Project project = projectService.getProjectByProjectID(projectID);

        // Check if the project exists
        if (project == null) {
            throw new IllegalArgumentException("Project not found.");
        }

        // Retrieve the sub-projects associated with the project ID
        List<SubProject> subProjects = subProjectService.getSubProjectsByProjectID(projectID);

        // Add the sub-projects, project, and CalcService to the model
        model.addAttribute("subProjects", subProjects);
        model.addAttribute("project", project);
        model.addAttribute("cs", calcService);

        // Return the view to display the sub-projects
        return "subprojectList";
    }


    // a GET method that shows the page for creating a sub project
    @GetMapping("/create/{projectID}")
    public String showCreateSubProjectPage(@PathVariable int projectID,
                                           HttpSession session,
                                           Model model) {
        // Retrieve the account from the ongoing session
        Account account = (Account) session.getAttribute("account");

        // Check if the account exists in session
        if (account == null) {
            throw new IllegalArgumentException("No account found in session. Please log in.");
        }

        // Retrieve the project based on project ID
        Project project = projectService.getProjectByProjectID(projectID);

        // Check if the project exists
        if (project == null) {
            throw new IllegalArgumentException("Project not found.");
        }

        // Add the project and a new sub-project to the model
        model.addAttribute("project", project);
        model.addAttribute("subProject", new SubProject());

        // Return the createSubproject page
        return "createSubproject";
    }



    // a POST method to create a subproject
    @PostMapping("/create")
    public String createSubProject(@ModelAttribute SubProject subproject,
                                   @RequestParam int projectID,
                                   HttpSession session,
                                   Model model) {
        // Retrieve the account from the ongoing session
        Account account = (Account) session.getAttribute("account");

        // Check if the account exists in session
        if (account == null) {
            throw new IllegalArgumentException("You need to log in to create a subproject.");
        }

        // Retrieve the project based on project ID
        Project project = projectService.getProjectByProjectID(projectID);

        // Check if the project exists
        if (project == null) {
            throw new IllegalArgumentException("Project not found.");
        }

        // Set the project ID for the subproject
        subproject.setProjectID(projectID);

        String subProjectDescription = subproject.getSubProjectDescription();

        // Generate task description using AI
        String aiPrompt = "This is a subproject, and the client is " + project.getProjectName() +
                ": Return an evaluation of " + subproject.getSubProjectName() + " described like this: " +
                subProjectDescription + ". The reader of the evaluation is a project manager, that wrote the subProject description." +
                "The project manager needs a time estimate, and ideas to split up the subproject into minor tasks to make it more feasible. Answer must be short. Max 400 characters!";

        // Let AI generate a time estimate and task breakdown
        String aiResponse = this.chatClient.prompt().user(aiPrompt).call().content();

        int maxLength = 500;
        if (aiResponse.length() > maxLength) {
            aiResponse = aiResponse.substring(0, maxLength).trim();
        }

        // Append the AI-generated content to the subproject description
        subProjectDescription = subProjectDescription + ". Consider this: " + aiResponse;
        subproject.setSubProjectDescription(subProjectDescription);

        // Save the new subproject
        subProjectService.createNewSubProject(subproject);

        // Retrieve updated list of subprojects for the project
        List<SubProject> subProjects = subProjectService.getSubProjectsByProjectID(projectID);

        // Add the project and subprojects to the model
        model.addAttribute("subProjects", subProjects);
        model.addAttribute("project", project);

        // Redirect to the subproject list for the project
        return "redirect:/projects/subprojects/" + projectID;
    }

    @GetMapping("/update/{subProjectID}")
    public String showUpdateSubProjectPage(@PathVariable int subProjectID,
                                           HttpSession session,
                                           Model model) {
        // Retrieve the account from the ongoing session
        Account account = (Account) session.getAttribute("account");

        // Check if the account exists in session
        if (account == null) {
            throw new IllegalArgumentException("You need to log in to update a subproject.");
        }

        // Retrieve the subproject by subProjectID
        SubProject subProject = subProjectService.getSubProjectBySubProjectID(subProjectID);

        // Check if the subproject exists
        if (subProject == null) {
            throw new IllegalArgumentException("Subproject not found.");
        }

        // Check if the user has permission to update this subproject
        if (account.getAccountType() != Account.AccountType.PROJECT_MANAGER &&
                account.getAccountType() != Account.AccountType.ADMIN) {
            throw new IllegalArgumentException("You don't have permission to update this subproject.");
        }

        // Add the subproject to the model for use in the view
        model.addAttribute("subProject", subProject);

        return "updateSubproject";
    }


@PostMapping("/update")
public String updateSubProject(@RequestParam int subProjectID,
                               @RequestParam String subProjectName,
                               @RequestParam String subProjectDescription,
                               @RequestParam LocalDate subProjectDeadline,
                               @RequestParam(value = "isFinished", defaultValue = "false") boolean isFinished,
                               HttpSession session) {
    // Retrieve the logged-in account
    Account account = (Account) session.getAttribute("account");

    // Check if the user is logged in
    if (account == null) {
        throw new IllegalArgumentException("Please log in to update a subproject.");
    }

    // Check if the user has the right permissions
    if (account.getAccountType() != Account.AccountType.ADMIN &&
            account.getAccountType() != Account.AccountType.PROJECT_MANAGER) {
        throw new IllegalArgumentException("You don't have permission to update subprojects.");
    }

    // Retrieve the existing subproject from the database
    SubProject existingSubProject = subProjectService.getSubProjectBySubProjectID(subProjectID);
    if (existingSubProject == null) {
        throw new IllegalArgumentException("Subproject not found.");
    }

    if (subProjectName == null || subProjectName.trim().isEmpty()) {
        throw new IllegalArgumentException("Subproject name cannot be empty.");
    }

    // Check that the deadline is not in the past
    if (subProjectDeadline.isBefore(LocalDate.now())) {
        throw new IllegalArgumentException("Deadline cannot be in the past.");
    }

    // Update subproject attributes
    existingSubProject.setSubProjectName(subProjectName);
    existingSubProject.setSubProjectDescription(subProjectDescription);
    existingSubProject.setSubProjectDeadline(subProjectDeadline);
    existingSubProject.setFinished(isFinished);

    // Save the updated subproject back to the database
    subProjectService.updateSubProject(existingSubProject);

    // Redirect to the subproject page with the updated details
    return "redirect:/projects/subprojects/" + existingSubProject.getProjectID();
}

}