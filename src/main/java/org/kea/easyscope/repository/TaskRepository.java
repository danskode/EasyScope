package org.kea.easyscope.repository;

import org.kea.easyscope.model.Account;
import org.kea.easyscope.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // this method retrieves all the task for a specific subproject
    public List<Task> getTasksBySubProjectID(int subProjectID) {
        // SQL-query for at hente task-data sammen med hours_estimated, hours_realized og assignedTo
        String sql = """
        SELECT t.task_id, t.task_name, t.task_description, t.task_is_finished, 
               t.sub_project_id_fk, 
               thr.task_hours_realized, 
               the.task_hours_estimated,
               a.account_id, a.account_name AS account_name
        FROM task t
        LEFT JOIN task_hours_realized thr ON t.task_id = thr.task_id_fk
        LEFT JOIN task_hours_estimated the ON t.task_id = the.task_id_fk
        LEFT JOIN task_member tm ON t.task_id = tm.task_id_fk
        LEFT JOIN accounts a ON tm.account_id_fk = a.account_id
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

            // Tildel det tildelte medlem, hvis der er en
            if (rs.getInt("account_id") != 0) { // Tjek for NULL eller tomme værdier
                Account assignedTo = new Account();
                assignedTo.setAccountID(rs.getInt("account_id"));
                assignedTo.setAccountName(rs.getString("account_name"));
                task.setAssignedTo(assignedTo);
            }

            return task;
        });
    }

    // method retrieves a list of tasks assigned to a specific team member
    public List<Task> getTasksAssignedTo(int accountID) {
        String sql = """
        SELECT t.task_id, t.task_name, t.task_description, 
               t.task_is_finished, t.sub_project_id_fk,
               thr.task_hours_realized, 
               the.task_hours_estimated
        FROM task t
        INNER JOIN task_member tm ON t.task_id = tm.task_id_fk
        LEFT JOIN task_hours_realized thr ON t.task_id = thr.task_id_fk
        LEFT JOIN task_hours_estimated the ON t.task_id = the.task_id_fk
        WHERE tm.account_id_fk = ?
    """;
        return jdbcTemplate.query(sql, new Object[]{accountID}, (rs, rowNum) -> {
            Task task = new Task();
            task.setTaskID(rs.getInt("task_id"));
            task.setTaskName(rs.getString("task_name"));
            task.setTaskDescription(rs.getString("task_description"));
            task.setTaskIsFinished(rs.getBoolean("task_is_finished"));
            task.setSubProjectID(rs.getInt("sub_project_id_fk"));

            // Set task hours (can be null)
            task.setRealizedHours(rs.getFloat("task_hours_realized"));
            task.setEstimatedHours(rs.getFloat("task_hours_estimated"));

            return task;

        });
    }

    // Method to create new task and set fill out some data in task_hours_realized and task_hours_estimated ...
    public Task createNewTask(Task task, int memberID, float estimatedHours) {
        String insertTaskSQL = """
            INSERT INTO task (task_name, task_description, task_is_finished, sub_project_id_fk, task_start_date) 
            VALUES (?, ?, ?, ?, ?)
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
            ps.setBoolean(3, task.isTaskIsFinished());
            ps.setInt(4, task.getSubProjectID());
            ps.setDate(5, task.getTaskStartDate() != null ? java.sql.Date.valueOf(task.getTaskStartDate()) : java.sql.Date.valueOf(LocalDate.now()));
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

    // method to finish an active task and set realized hours
    public Task finishTask(Task task, boolean isFinished, float realizedHours) {
        String updateTaskSQL = """
                UPDATE task
                SET task_is_finished = ?
                WHERE task_id = ?
                """;

        String setRealizedHoursSQL = """
                UPDATE task_hours_realized
                SET task_hours_realized = ?
                WHERE task_id_fk = ?
                """;

        jdbcTemplate.update(updateTaskSQL, isFinished, task.getTaskID());
        jdbcTemplate.update(setRealizedHoursSQL, realizedHours, task.getTaskID());
        return task;
    }

    // retrieves a list of finished tasks
    public List<Task> getFinishedTasks(int accountID) {
        String sql = """
        SELECT t.task_id, t.task_name, t.task_description, t.task_is_finished,
               te.task_hours_estimated AS estimated_hours,
               tr.task_hours_realized AS realized_hours
        FROM task t
        LEFT JOIN task_hours_estimated te ON t.task_id = te.task_id_fk
        LEFT JOIN task_hours_realized tr ON t.task_id = tr.task_id_fk
        INNER JOIN task_member tm ON t.task_id = tm.task_id_fk
        WHERE t.task_is_finished = 1
        AND tm.account_id_fk = ?
    """;

        return jdbcTemplate.query(sql, new Object[]{accountID}, (rs, rowNum) -> {
            Task task = new Task();
            task.setTaskID(rs.getInt("task_id"));
            task.setTaskName(rs.getString("task_name"));
            task.setTaskDescription(rs.getString("task_description"));
            task.setTaskIsFinished(rs.getBoolean("task_is_finished"));
            task.setEstimatedHours(rs.getFloat("estimated_hours"));
            task.setRealizedHours(rs.getFloat("realized_hours"));
            return task;
        });
    }



    public Task updateTask(Task task, int memberID, float estimatedHours) {
        // SQL statement that updates a task
        String updateTaskSQL = """
            UPDATE task
            SET task_name = ?, task_description = ?, sub_project_id_fk = ?, task_start_date = ?
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
        jdbcTemplate.update(updateTaskSQL, task.getTaskName(), task.getTaskDescription(), task.getSubProjectID(), task.getTaskStartDate(), task.getTaskID());
        // updates the team member
        jdbcTemplate.update(updateTeamMemberSQL, memberID, task.getTaskID());
        // updates estimated hours
        jdbcTemplate.update(updateEstimatedHoursSQL, estimatedHours, task.getTaskID());

        // returns the updated task object
        return task;
    }

    public Task getTaskByID(int taskID) {
        String sql = """
        SELECT t.task_id, t.task_name, t.task_description, t.task_is_finished, t.sub_project_id_fk, t.task_start_date,
               the.task_hours_estimated
        FROM task t
        LEFT JOIN task_hours_estimated the ON t.task_id = the.task_id_fk
        WHERE t.task_id = ?
        """;

        return jdbcTemplate.queryForObject(sql, new Object[]{taskID}, (rs, rowNum) -> {
            Task task = new Task();
            task.setTaskID(rs.getInt("task_id"));
            task.setTaskName(rs.getString("task_name"));
            task.setTaskDescription(rs.getString("task_description"));
            task.setTaskIsFinished(rs.getBoolean("task_is_finished"));
            task.setSubProjectID(rs.getInt("sub_project_id_fk"));
            task.setTaskStartDate(rs.getDate("task_start_date").toLocalDate());
            // from "mellemtabel" task_hours_estimated
            task.setEstimatedHours(rs.getFloat("task_hours_estimated"));
            return task;
        });
    }


    // Delete methods
    // delete task_id foreign key from tabels before you delete a task
    public void deleteTaskMembers(int taskID) {
        String sql = "DELETE FROM task_member WHERE task_id_fk = ?";
        jdbcTemplate.update(sql, taskID);
    }

    public void deleteTaskHoursRealized(int taskID) {
        String sql = "DELETE FROM task_hours_realized WHERE task_id_fk = ?";
        jdbcTemplate.update(sql, taskID);
    }

    public void deleteTask(int taskID) {
        String sql = "DELETE FROM task WHERE task_id = ?";
        jdbcTemplate.update(sql, taskID);
    }





}
