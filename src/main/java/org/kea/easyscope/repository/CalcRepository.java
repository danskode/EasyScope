package org.kea.easyscope.repository;

import org.kea.easyscope.model.Project;
import org.kea.easyscope.model.SubProject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

        // Get numbers of team members associated with sub project ...
        int numMembers = numbersOfTeamMembersOnSubProject(subProject);

        // Each team member can work for 7 hours a day
        numMembers = numMembers * 7;

        LocalDate deadline = subProject.getSubProjectDeadline();
        long daysLeftOnTaskHours = (long) Math.ceil(getTotalHoursForASubProject(subProject) / numMembers );
        LocalDate today = LocalDate.now();
        LocalDate daysLeft = today.plusDays(daysLeftOnTaskHours);

        // Define the date format ...
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

    // Sum up all hours left (estimated) for a subproject and see if a project is on track dived by numbers of team members associated with sub project ...
    public boolean isAnyTasksInProjectExceedingDeadline(Project project) {
        int projectID = project.getProjectID();

        String sql = "SELECT sp.sub_project_deadline, " +
                "SUM(the.task_hours_estimated) AS total_estimated_hours " +
                "FROM project p " +
                "JOIN sub_project sp ON p.project_id = sp.project_id_fk " +
                "JOIN task t ON sp.sub_project_id = t.sub_project_id_fk " +
                "JOIN task_hours_estimated the ON t.task_id = the.task_id_fk " +
                "WHERE p.project_id = ? AND sp.sub_project_is_finished = 0 " +
                "GROUP BY sp.sub_project_id, sp.sub_project_deadline";

        // Execute the query
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, projectID);

        // Current date ...
        LocalDate currentDate = LocalDate.now();

        // Iterate over the results and check if any sub-project exceeds its deadline ...
        for (Map<String, Object> row : results) {
            LocalDate subProjectDeadline = ((java.sql.Date) row.get("sub_project_deadline")).toLocalDate();
            long totalEstimatedHours = ((Number) row.get("total_estimated_hours")).longValue();

            // Calculate projected completion date
            long projectedDays = (long) Math.ceil(totalEstimatedHours / 7.0);
            LocalDate projectedCompletionDate = currentDate.plusDays(projectedDays);

            // Check if the deadline is before the projected completion date
            if (subProjectDeadline.isBefore(projectedCompletionDate)) {
                return true; // At least one sub-project exceeds its deadline
            }
        }

        return false; // No sub-project exceeds its deadline
    }

    // Sum up all hours left (estimated) for a single subproject ...
    public float getTotalHoursForASubProject(SubProject subProject){
        int subProjectID = subProject.getSubProjectID();
        // SQL statement to get all active tasks' estimated hours from a specific subProjectID ...
        String sql = "SELECT the.task_hours_estimated " +
                "FROM task_hours_estimated the " +
                "JOIN task t ON the.task_id_fk = t.task_id " +
                "JOIN sub_project sp ON t.sub_project_id_fk = sp.sub_project_id " +
                "WHERE t.sub_project_id_fk = ? AND t.task_is_finished=0";
        //Here's a list to collect all estemated hours per task for subproject ...
        List<Float> totalHoursEstimatedOnSubProject = new ArrayList<>();

        // Here we do the sql magic with a Lampda for the total hours estimated part ...
        jdbcTemplate.query(sql, new Object[]{subProjectID}, (rs) -> {
            Float totalHours = rs.getFloat("task_hours_estimated");
            totalHoursEstimatedOnSubProject.add(totalHours);
        });

        float totalHoursForSubProject = 0.0F;

        for( float theosp : totalHoursEstimatedOnSubProject) {
            totalHoursForSubProject += theosp;
        }
        return totalHoursForSubProject;
    }

    // Sum up all hours left (estimated) for a single subproject ...
    public float getTotalHoursRealizedForASubProject(SubProject subProject){
        int subProjectID = subProject.getSubProjectID();
        // SQL statement to get all active tasks' estimated hours from a specific subProjectID ...
        String sql = "SELECT thr.task_hours_realized " +
                "FROM task_hours_realized thr " +
                "JOIN task t ON thr.task_id_fk = t.task_id " +
                "JOIN sub_project sp ON t.sub_project_id_fk = sp.sub_project_id " +
                "WHERE t.sub_project_id_fk = ? AND t.task_is_finished=1";

        //Here's a list to collect all estemated hours per task for subproject ...
        List<Float> totalHoursRealizedOnSubProject = new ArrayList<>();

        // Here we do the sql magic with a Lampda for the total hours estimated part ...
        jdbcTemplate.query(sql, new Object[]{subProjectID}, (rs) -> {
            Float totalHours = rs.getFloat("task_hours_realized");
            totalHoursRealizedOnSubProject.add(totalHours);
        });

        float totalHoursRealizedForSubProject = 0.0F;

        for( float throsp : totalHoursRealizedOnSubProject) {
            totalHoursRealizedForSubProject += throsp;
        }
        return totalHoursRealizedForSubProject;
    }

    // Count numbers of team members associated with a specific subproject ...
    public int numbersOfTeamMembersOnSubProject(SubProject subProject) {
        int subProjectID = subProject.getSubProjectID();

        String sql =    "SELECT COUNT(DISTINCT the.account_id_fk) AS distinct_account_count " +
                        "FROM task_hours_estimated the " +
                        "JOIN task t ON the.task_id_fk = t.task_id " +
                        "JOIN sub_project sp ON t.sub_project_id_fk = sp.sub_project_id " +
                        "WHERE sp.sub_project_id = ? AND t.task_is_finished=0";

        // Query for a single result ...
        Integer distinctAccountCount = jdbcTemplate.queryForObject(sql, new Object[]{subProjectID}, Integer.class);

        // If no accounts are found, return 1 to not break math ...
        return distinctAccountCount != null ? distinctAccountCount : 1;
    }

}
