package org.kea.easyscope.repository;

import org.kea.easyscope.model.Project;
import org.kea.easyscope.model.SubProject;
import org.kea.easyscope.service.SubProjectService;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class CalcRepository {

    private final SubProjectService subProjectService;

    public CalcRepository(SubProjectService subProjectService) {
        this.subProjectService = subProjectService;
    }

    public String giveNoticeAboutDeadline(int subProjectID) {
        SubProject subProject = subProjectService.getSubProjectBySubProjectID(subProjectID);
        if (subProject != null) {
                LocalDate deadline = subProject.getSubProjectDeadline();
                Long daysLeftOnTaskHours = ((long)subProject.getTotalEstimatedHours())/7;
                LocalDate daysLeft = LocalDate.now().plusDays( daysLeftOnTaskHours);

                if (deadline.isBefore(daysLeft)) {
                    return "You need to add more team members to this assignment to make it within deadline!";
                }
                if (deadline.isAfter(daysLeft)) {
                    return "You are on schedule!";
                }
            }
        return "You are missing a subProjectID to continue";
    }
}
