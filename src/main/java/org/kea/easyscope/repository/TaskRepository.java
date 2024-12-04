package org.kea.easyscope.repository;

import org.kea.easyscope.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // this method retrieves all the task for a specific subproject
    public List<Task> getTasksBySubProjectID(int subProjectID) {
        String sql = "SELECT * FROM task WHERE subproject_id = ?";

        return jdbcTemplate.query(sql, new Object[]{subProjectID}, (rs, rowNum) -> {
            Task task = new Task();
            task.setTaskID(rs.getInt("task_id"));
            task.setTaskName(rs.getString("task_name"));
            task.setTaskIsFinished(rs.getBoolean("task_is_finished"));
            task.setSubProjectID(rs.getInt("subproject_id"));
        return task;
        });
    }

    // metode til at hente en task ud fra et task_id

    // metode til at tilføje en ny task





}
