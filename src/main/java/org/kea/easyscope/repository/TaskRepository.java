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
        // SQL-query for at hente task-data sammen med hours_estimated og hours_realized
        String sql = """
        SELECT t.task_id, t.task_name, t.task_description, t.task_is_finished, 
               t.sub_project_id_fk, 
               thr.task_hours_realized, 
               the.task_hours_estimated
        FROM task t
        LEFT JOIN task_hours_realized thr ON t.task_id = thr.task_id_fk
        LEFT JOIN task_hours_estimated the ON t.task_id = the.task_id_fk
        WHERE t.sub_project_id_fk = ?
    """;

        // Brug JdbcTemplate til at udføre query og mappe resultaterne
        return jdbcTemplate.query(sql, new Object[]{subProjectID}, (rs, rowNum) -> {
            Task task = new Task();
            task.setTaskID(rs.getInt("task_id"));
            task.setTaskName(rs.getString("task_name"));
            task.setTaskDescription(rs.getString("task_description"));
            task.setTaskIsFinished(rs.getBoolean("task_is_finished"));
            task.setSubProjectID(rs.getInt("sub_project_id_fk"));

            // Tilføj de enkelte kolonner for realized og estimated
            task.setRealizedHours(rs.getFloat("task_hours_realized")); // Kan være NULL
            task.setEstimatedHours(rs.getFloat("task_hours_estimated")); // Kan være NULL

            return task;
        });
    }

    // metode til at hente en task ud fra et task_id

    // metode til at tilføje en ny task





}
