package org.kea.easyscope.repository;

import org.kea.easyscope.model.SubProject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SubProjectRepository {

    private final JdbcTemplate jdbcTemplate;

    public SubProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SubProject> getSubProjectsByProjectID(int projectID) {
        // First of we get all the hours from the tasks from the database and SUM them up ...
        String taskHoursSql = "SELECT t.sub_project_id_fk, SUM(DISTINCT the.task_hours_estimated) AS total_estimated_hours " +
                "FROM task t " +
                "LEFT JOIN task_hours_estimated the ON t.task_id = the.task_id_fk " +
                "GROUP BY t.sub_project_id_fk";

        // Here we use a (Hash)Map to save the SUM'med total hours above into from every subproject (id is the int)
        Map<Integer, Float> subProjectHoursMap = new HashMap<>();

        // Here we do the sql magic with a Lampda for the total hours estimated part ...
        jdbcTemplate.query(taskHoursSql, new Object[]{}, (rs) -> {
            int subProjectID = rs.getInt("sub_project_id_fk");
            Float totalHours = rs.getFloat("total_estimated_hours");
            subProjectHoursMap.put(subProjectID, totalHours);
        });

        // We also need to do the same for realized hours per subproject ...
        String taskRealizedHoursSql = "SELECT t.sub_project_id_fk, SUM(DISTINCT thr.task_hours_realized) AS total_realized_hours " +
                "FROM task t " +
                "LEFT JOIN task_hours_realized thr ON t.task_id = thr.task_id_fk " +
                "GROUP BY t.sub_project_id_fk";
        Map<Integer, Float> subProjectHoursRealizedMap = new HashMap<>();
        jdbcTemplate.query(taskRealizedHoursSql, new Object[]{}, (rs) -> {
            int subProjectID = rs.getInt("sub_project_id_fk");
            Float totalHours = rs.getFloat("total_realized_hours");
            subProjectHoursRealizedMap.put(subProjectID, totalHours);
        });

        // Then we write the call to get the rest of the subproject object from the database ...
        String sql = "SELECT * FROM sub_project WHERE project_id_fk = ?";

        //  Then we use rowmapper to map the result to SubProject objects ...
        return jdbcTemplate.query(sql, new Object[]{projectID}, new RowMapper<SubProject>() {
            public SubProject mapRow(ResultSet rs, int rowNum) throws SQLException {
                SubProject subProject = new SubProject();
                int subProjectID = rs.getInt("sub_project_id");

                subProject.setSubProjectID(subProjectID);
                subProject.setSubProjectName(rs.getString("sub_project_name"));
                subProject.setSubProjectDescription(rs.getString("sub_project_description"));
                subProject.setSubProjectDeadline(rs.getDate("sub_project_deadline").toLocalDate());
                subProject.setFinished(rs.getBoolean("sub_project_is_finished"));
                subProject.setProjectID(rs.getInt("project_id_fk"));

                // Then we combine the HasMap of total_estimated_hours with into the rowmapping ...
                Float totalEstimatedHours = subProjectHoursMap.getOrDefault(subProjectID, 0.0F);
                subProject.setTotalEstimatedHours(totalEstimatedHours);

                // And we combine the HasMap of total_realized_hours with into the rowmapping ...
                Float totalRealizedHours = subProjectHoursRealizedMap.getOrDefault(subProjectID, 0.0F);
                subProject.setTotalRealizedHours(totalRealizedHours);

                // And finally we can return a complete SubProject object
                return subProject;
            }
        });
    }



    // a method to retrieve a subproject object based on subProjectID
    public SubProject getSubProjectBySubProjectID(int subProjectID) {
        String sql = "SELECT * FROM sub_project WHERE sub_project_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{subProjectID}, (rs, rowNum) -> {
            SubProject subProject = new SubProject();
            subProject.setSubProjectID(rs.getInt("sub_project_id"));
            subProject.setSubProjectName(rs.getString("sub_project_name"));
            subProject.setSubProjectDescription(rs.getString("sub_project_description"));
            subProject.setSubProjectDeadline(rs.getDate("sub_project_deadline").toLocalDate());
            subProject.setFinished(rs.getBoolean("sub_project_is_finished"));
            subProject.setProjectID(rs.getInt("project_id_fk"));
            return subProject;
        });
    }

    // method to create a new sub project in the db
    public void createNewSubProject(SubProject subProject) {
        // SQL statement to insert a new sub project into the db
        String sql = "INSERT INTO sub_project (sub_project_name, sub_project_description, sub_project_deadline, project_id_fk)VALUES (?, ?, ?, ?)";
        // this executes the update by inserting values from the sub project object into the db
        jdbcTemplate.update(sql, subProject.getSubProjectName(), subProject.getSubProjectDescription(), subProject.getSubProjectDeadline(), subProject.getProjectID());
    }

    // method to update a subproject
    public void updateSubProject(SubProject subProject) {
        String sql = "UPDATE sub_project SET sub_project_name = ?, sub_project_description = ?, sub_project_deadline = ?, sub_project_is_finished = ? WHERE sub_project_id = ?";
        jdbcTemplate.update(sql, subProject.getSubProjectName(), subProject.getSubProjectDescription(), subProject.getSubProjectDeadline(), subProject.isFinished(), subProject.getProjectID());
    }

// metode der gør at team member kan se sin opgave og indsætte realiserede timetal og arkivere

    // en delete funktion som kun viser sig hvis task

    }


