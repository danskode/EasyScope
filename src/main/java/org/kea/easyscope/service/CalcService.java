package org.kea.easyscope.service;

import org.kea.easyscope.model.Project;
import org.kea.easyscope.model.SubProject;
import org.kea.easyscope.model.Task;
import org.kea.easyscope.repository.CalcRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalcService {
    CalcRepository calcRepository;

    public CalcService(CalcRepository calcRepository, ProjectService projectService,SubProjectService subProjectService, TaskService taskService, AccountService accountService) {
        this.calcRepository = calcRepository;
    }

    // Add deadline feedback to subprojectList.html
    public String giveNoticeAboutDeadline(SubProject subProject){
        return calcRepository.giveNoticeAboutDeadline(subProject);
    }

    // Methode to sum up total of realized hours for all tasks  belonging to a subproject still active ...
    public float getTotalHoursForASubProject(SubProject subProject){
        return calcRepository.getTotalHoursForASubProject(subProject);
    }

    public boolean isAnyTasksInProjectExceedingDeadline(Project project) throws SQLException {
        return calcRepository.isAnyTasksInProjectExceedingDeadline(project);
    }

    public float getTotalHoursRealizedForASubProject(SubProject subProject){
        return calcRepository.getTotalHoursRealizedForASubProject(subProject);
    }

    public int numbersOfTeamMembersOnSubProject(SubProject subProject){
        return calcRepository.numbersOfTeamMembersOnSubProject(subProject);
    }
}
