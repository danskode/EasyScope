package org.kea.easyscope.service;

import org.kea.easyscope.model.Project;
import org.kea.easyscope.model.SubProject;
import org.kea.easyscope.model.Task;
import org.kea.easyscope.repository.CalcRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class CalcService {
    CalcRepository calcRepository;

    public CalcService(CalcRepository calcRepository, ProjectService projectService,SubProjectService subProjectService, TaskService taskService, AccountService accountService) {
        this.calcRepository = calcRepository;
    }

    public String giveNoticeAboutDeadline(SubProject subProject){
        return calcRepository.giveNoticeAboutDeadline(subProject);
    }

    // Methode to sum up total of realized hours for all tasks  belonging to a subproject still active ...

    // Methode to sum up total of realized hours for all tasks  belonging to a subproject been archived/finished ...

    // Methode to sum up estimated hours for all subprojects in a specific project ...

    // Methode to sum up realized hours for all subprojects in a specific project ...

    // Method to show all hours spend on client/project historically (active projects + archived) ...

    // Method to check if a specific projects subprojects keeping up with deadlines set for subproject ...
//    public boolean isSubprojectKeepingDeadline(int projectID) {
//
//        SubProject subProject = subProjectService.get
//        List<Task> task = subProjectService.getSubProjectsByProjectID(projectID);
//
//        LocalDate today = LocalDate.now();
//        LocalDate deadline = project.
//
//        // int daysTillDeadline ...

}
