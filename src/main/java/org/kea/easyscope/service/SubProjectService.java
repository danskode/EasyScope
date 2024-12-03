package org.kea.easyscope.service;

import org.kea.easyscope.model.SubProject;
import org.kea.easyscope.repository.SubProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubProjectService {

    private final SubProjectRepository subProjectRepository;

    public SubProjectService(SubProjectRepository subProjectRepository) {
        this.subProjectRepository = subProjectRepository;
    }

    public List<SubProject> getSubProjectsByProjectID(int projectID) {
        return subProjectRepository.getSubProjectsByProjectID(projectID);
    }

    public SubProject getSubProjectBySubProjectID(int subProjectID) {
        return subProjectRepository.getSubProjectBySubProjectID(subProjectID);
    }

    public void createNewSubProject(SubProject subProject) {
        subProjectRepository.createNewSubProject(subProject);
    }

    public void updateSubProject(SubProject subProject) {
        subProjectRepository.updateSubProject(subProject);
    }

}

