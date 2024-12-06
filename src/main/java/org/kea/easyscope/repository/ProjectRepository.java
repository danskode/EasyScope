package org.kea.easyscope.repository;

import org.kea.easyscope.model.Project;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectRepository {

    // here we declare JdbcTemplate which is used for db communication
    private final JdbcTemplate jdbcTemplate;

    // constructor to inject JdbcTemplate dependency
    public ProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // method to get a list of projects for a specific accountID
    public List<Project> getProjectFromAccountID(int accountID) {
        String sql = "SELECT * FROM project WHERE account_id_fk = ?";
        return jdbcTemplate.query(sql, new Object[]{accountID}, (rs, rowNum) -> { // LAMBDA....
            Project project = new Project();
            project.setProjectID(rs.getInt("project_id"));
            project.setProjectName(rs.getString("project_name"));
            project.setProjectDescription(rs.getString("project_description"));
            project.setFinished(rs.getBoolean("project_is_finished"));
            project.setAccountID(rs.getInt("account_id_fk"));
            return project;
        });
    }

    // method to retrieve a project based on projectID
    public Project getProjectByProjectID(int projectID) {
        String sql = "SELECT * FROM project WHERE project_id=?";

        return jdbcTemplate.queryForObject(sql, new Object[]{projectID}, (rs, rowNum) -> { // LAMBDA..
            Project project = new Project();
            project.setProjectID(rs.getInt("project_id"));
            project.setProjectName(rs.getString("project_name"));
            project.setProjectDescription(rs.getString("project_description"));
            project.setFinished(rs.getBoolean("project_is_finished"));
            project.setAccountID(rs.getInt("account_id_fk"));
            return project;
        });
    }

    // method to create a new project in the db
    public void createNewProject(Project project) {
        // SQL statement to insert a new project into the db
        String sql = "INSERT INTO project (project_name, project_description, project_is_finished, account_id_fk) VALUES (?, ?, ?, ?)";
        // this executes the update by inserting values from the project object into the db
        jdbcTemplate.update(sql, project.getProjectName(), project.getProjectDescription(), project.isFinished(), project.getAccountID());
    }

    // method to update a project
    public void updateProject(Project project) {
        String sql = "UPDATE project SET project_name = ?, project_description = ?, project_is_finished = ? WHERE project_id = ?";
        jdbcTemplate.update(sql, project.getProjectName(), project.getProjectDescription(), project.isFinished(), project.getProjectID());
    }

}
