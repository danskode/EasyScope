package org.kea.easyscope.controller;

import org.kea.easyscope.model.Project;
import org.kea.easyscope.model.SubProject;
import org.kea.easyscope.service.ProjectService;
import org.kea.easyscope.service.SubProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

            // sendt subProjects and project to the formular
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


    // a POST method to create a sub project
    @PostMapping("/create")
    public String createSubProject(@ModelAttribute SubProject subproject, @RequestParam int projectID, Model model) {
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

}