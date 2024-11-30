package org.kea.easyscope.service;

import org.kea.easyscope.model.Project;
import org.kea.easyscope.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    // we declare a ProjectRepository to interact with the db
    private final ProjectRepository projectRepository;

    // a constructor to inject ProjectRepository dependency
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // method to get a list of projects for a specific accountID by calling the repository method!
    public List<Project> getProjectFromAccountID(int accountID) {
        // this passes the accountID to the repository to get the projects
        return projectRepository.getProjectFromAccountID(accountID);
    }

    public Project getProjectByProjectID(int projectID) {
        return projectRepository.getProjectByProjectID(projectID);
    }

    // method to create a new project by calling the repository method
    public void createNewProject(Project project) {
        // calls the repository to insert the new project into the db
        projectRepository.createNewProject(project);
    }

    // method to update an existing project by calling the repository method
    public void updateProject(Project project) {
        projectRepository.updateProject(project);
    }
}
