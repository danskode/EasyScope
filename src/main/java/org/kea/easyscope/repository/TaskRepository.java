package org.kea.easyscope.repository;

import org.kea.easyscope.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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

    // a project manager can assign a task to a team member -- bliver ikke brugt endnu
    public void assignTaskToMember(int taskID, int accountID) {
        String sql = "INSERT INTO task_member (task_id_fk, account_id_fk) VALUES (?, ?)";
        jdbcTemplate.update(sql, taskID, accountID);
    }

    // metode til at hente en task ud fra et task_id

    // Method to create new task and set fill out some data in task_hours_realized and task_hours_estimated ...
    public Task createNewTask(Task task, int memberID, float estimatedHours) {

        String insertTaskSQL = """
            INSERT INTO task (task_name, task_description, sub_project_id_fk, task_is_finished) 
            VALUES (?, ?, ?, 0)
            """;

        String assignTeamMemberSQL = """
            INSERT INTO task_member (task_id_fk, account_id_fk)
            VALUES (?, ?)
            """;

        String insertEstimatedHoursSQL = """
            INSERT INTO task_hours_estimated (task_id_fk, task_hours_estimated, account_id_fk)
            VALUES (?, ?, ?)
            """;

        String insertRealizedHoursSQL = """
            INSERT INTO task_hours_realized (task_id_fk, task_hours_realized, account_id_fk)
            VALUES (?, 0.0, ?)
            """;

        // KeyHolder for capturing auto-generated task ID
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // Create task and capture the generated taskID using KeyHolder
        jdbcTemplate.update(connection -> {
            // Create a prepared statement with auto-generated key (task_id)
            var ps = connection.prepareStatement(insertTaskSQL, new String[] { "task_id" });
            ps.setString(1, task.getTaskName());
            ps.setString(2, task.getTaskDescription());
            ps.setInt(3, task.getSubProjectID());
            return ps;
        }, keyHolder);

        // Get the generated task ID from the KeyHolder
        int taskID = keyHolder.getKey().intValue();

        // Assign the team member to the task
        jdbcTemplate.update(assignTeamMemberSQL, taskID, memberID);

        // Insert the estimated hours for the task
        jdbcTemplate.update(insertEstimatedHoursSQL, taskID, estimatedHours, memberID);

        // Set realized hours for the task to zero ...
        jdbcTemplate.update(insertRealizedHoursSQL, taskID, memberID);

        // Return the task with the generated taskID
        task.setTaskID(taskID);
        return task;
    }



    public Task updateTask(Task task, int memberID, float estimatedHours) {
        // SQL statement that updates a task
        String updateTaskSQL = """
            UPDATE task
            SET task_name = ?, task_description = ?, sub_project_id_fk = ?
            WHERE task_id = ?
            """;

        // SQL statement that updates the assigned team member
        String updateTeamMemberSQL = """
            UPDATE task_member
            SET account_id_fk = ?
            WHERE task_id_fk = ?
            """;

        // SQL statement that updates estimated hours
        String updateEstimatedHoursSQL = """
            UPDATE task_hours_estimated
            SET task_hours_estimated = ?
            WHERE task_id_fk = ?
            """;

        // Updates the tasks info
        jdbcTemplate.update(updateTaskSQL, task.getTaskName(), task.getTaskDescription(), task.getSubProjectID(), task.getTaskID());
        // updates the team member
        jdbcTemplate.update(updateTeamMemberSQL, memberID, task.getTaskID());
        // updates estimated hours
        jdbcTemplate.update(updateEstimatedHoursSQL, estimatedHours, task.getTaskID());

        // returns the updated task object
        return task;
    }

    public Task getTaskById(int taskID) {
        String sql = "SELECT * FROM task WHERE task_id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{taskID}, (rs, rowNum) -> {
            Task task = new Task();
            task.setTaskID(rs.getInt("task_id"));
            task.setTaskName(rs.getString("task_name"));
            task.setTaskDescription(rs.getString("task_description"));
            task.setTaskIsFinished(rs.getBoolean("task_is_finished"));
            task.setSubProjectID(rs.getInt("sub_project_id_fk"));
            return task;
        });
    }



}
