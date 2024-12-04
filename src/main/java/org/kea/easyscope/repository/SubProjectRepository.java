package org.kea.easyscope.repository;

import org.kea.easyscope.model.SubProject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubProjectRepository {

    private final JdbcTemplate jdbcTemplate;

    public SubProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // a method to retrieve a list of subprojects based on projectID
    public List<SubProject> getSubProjectsByProjectID(int projectID) {
        String sql = "SELECT * FROM sub_project WHERE project_id_fk = ?";

        return jdbcTemplate.query(sql, new Object[]{projectID}, (rs, rowNum) -> {
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



    }


