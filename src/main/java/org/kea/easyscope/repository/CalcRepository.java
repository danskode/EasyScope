package org.kea.easyscope.repository;

import org.kea.easyscope.model.Project;
import org.kea.easyscope.model.SubProject;
import org.kea.easyscope.service.SubProjectService;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class CalcRepository {

    private final SubProjectService subProjectService;

    public CalcRepository(SubProjectService subProjectService) {
        this.subProjectService = subProjectService;
    }

    // Working method to get a notice about deadline for tasks / subproject ...
    public String giveNoticeAboutDeadline(SubProject subProject) {

       // if (subProject != null) {
            LocalDate deadline = subProject.getSubProjectDeadline();
            long daysLeftOnTaskHours = (long) Math.ceil(subProject.getTotalEstimatedHours() / 7);
            LocalDate today = LocalDate.now();
            LocalDate daysLeft = today.plusDays(daysLeftOnTaskHours);

            // Define the formatter with the desired pattern
            DateTimeFormatter europeanFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            if(deadline.isBefore(daysLeft)) {
                return "You need to add more team members to this assignment to make it within deadline! You will be finished at " + daysLeft.format(europeanFormatter);
            }
            if(deadline.isAfter(daysLeft)) {
                return "You are on schedule! You will be finished at " + daysLeft.format(europeanFormatter);
            }
            if (deadline.isEqual(daysLeft)) {
                return "You are RIGHT on schedule!";
            }
            else {
                return "";
            }
    }


}
