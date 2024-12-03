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

    @GetMapping("/{projectID}")
    public String showSubProjectList(@PathVariable int projectID, Model model) {
        Project project = projectService.getProjectByProjectID(projectID);

        if (project != null) {
            List<SubProject> subProjects = subProjectService.getSubProjectsByProjectID(projectID);
            model.addAttribute("subProjects", subProjects);
            model.addAttribute("project", project);
            return "subProjectList";
        } else {
            return "redirect:/projectList";
        }
    }

    @GetMapping("/create/{projectID}")
    public String showCreateSubProjectPage(@PathVariable int projectID, Model model) {
        Project project = projectService.getProjectByProjectID(projectID);

        if (project == null) {
            model.addAttribute("error", "Project not found.");
            return "redirect:/projectList";
        }

        model.addAttribute("project", project);

        model.addAttribute("subProject", new SubProject());
        return "createSubProject";
    }

    @PostMapping("/create")
    public String createSubProject(@ModelAttribute SubProject subproject) {
        subProjectService.createNewSubProject(subproject);
        return "redirect:/subProjectList";
    }

}