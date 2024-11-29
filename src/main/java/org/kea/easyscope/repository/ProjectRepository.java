package org.kea.easyscope.repository;

import org.kea.easyscope.model.Project;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        // SQL statement to select all projects for a specific accountID
        String sql = "SELECT * FROM project WHERE account_id_fk=?";

        // RowMapper is used so we can map each row from the resultset to a project object
        RowMapper<Project> rowMapper = new RowMapper<Project>() {
            public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Project(
                        rs.getString("project_name"),
                        rs.getString("project_description"),
                        rs.getBoolean("project_is_active"),
                        rs.getInt("account_id_fk")
                );
            }
        }; // end of definition on how to map the rows from the database to project objects

        // we send the request and a list of projects is returned
        return jdbcTemplate.query(sql, rowMapper, accountID);
    }

    // method to create a new project in the db
    public void createNewProject(Project project) {
        // SQL statement to insert a new project into the db
        String sql = "INSERT INTO project (project_name, project_description, account_id_fk) VALUES (?, ?, ?)";
        // this executes the update by inserting values from the project object into the db
        jdbcTemplate.update(sql, project.getProjectName(), project.getProjectDescription(), project.getAccountID());
    }
}
