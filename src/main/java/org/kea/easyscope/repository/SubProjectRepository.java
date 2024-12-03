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

    public List<SubProject> getSubProjectsByProjectID(int projectID) {
        String sql = "SELECT * FROM sub_project WHERE sub_project_id = ?";

        return jdbcTemplate.query(sql, new Object[]{projectID}, (rs, rowNum) -> {
            SubProject subProject = new SubProject();
            subProject.setSubProjectID(rs.getInt("sub_project_id"));
            subProject.setSubProjectName(rs.getString("sub_project_name"));
            subProject.setSubProjectDescription(rs.getString("sub_project_description"));
            subProject.setSubProjectDeadline(rs.getDate("sub_project_deadline").toLocalDate());
            subProject.setProjectID(rs.getInt("project_id_fk"));
            return subProject;

        });
    }

    // getSubProjectByAccountID???

    // method to create a new sub project in the db
    public void createNewSubProject(SubProject subProject) {
        // SQL statement to insert a new sub project into the db
        String sql = "INSERT INTO sub_project VALUES (?, ?, ?, ?, ?)";
        // this executes the update by inserting values from the sub project object into the db
        jdbcTemplate.update(sql, subProject.getSubProjectName(), subProject.getSubProjectDescription(), subProject.getSubProjectDeadline(), subProject.getProjectID());
    }



    }


