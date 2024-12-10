package org.kea.easyscope.repository;

import org.kea.easyscope.model.Project;
import org.kea.easyscope.model.SubProject;
import org.kea.easyscope.service.SubProjectService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CalcRepository {

    private final JdbcTemplate jdbcTemplate;

    public CalcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    // Sum up all hours left (Estimated) for a subproject ...
    public float getTotalHoursForASubProject(SubProject subProject){
        int subProjectID = subProject.getSubProjectID();
        // SQL statement to get all active tasks' estimated hours from a specific subProjectID ...
        String sql="SELECT t.task_id, the.task_hours_estimated " +
                "FROM task " +
                "JOIN sub_project sp " +
                "JOIN task_hours_estimated the " +
                "WHERE t.sub_project_id_fk=? AND the.task_id_fk=t.task_id";

        // Here we use a (Hash)Map to save the SUM'med total hours above into from every subproject (id is the int)
        // Map<Integer, Float> subProjectHoursMap = new HashMap<>();
        List<Float> totalHoursEstimatedOnSubProject = new ArrayList<>();

        // Here we do the sql magic with a Lampda for the total hours estimated part ...
        jdbcTemplate.query(sql, new Object[]{}, (rs) -> {
            Float totalHours = rs.getFloat("total_estimated_hours");
            totalHoursEstimatedOnSubProject.add(totalHours);
        });

        float totalHoursForSubProject = 0.0F;

        for( float theosp : totalHoursEstimatedOnSubProject) {
            totalHoursForSubProject += theosp;
        }
        return totalHoursForSubProject;
    }


}
