package org.kea.easyscope.service;

import org.kea.easyscope.model.Task;
import org.kea.easyscope.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    public List<Task> getTasksBySubProjectID(int subProjectID){
        return taskRepository.getTasksBySubProjectID(subProjectID);
    }

    public Task createNewTask(Task task, int memberID, float estimatedHours){
        return taskRepository.createNewTask(task, memberID, estimatedHours);
    }

    public Task getTaskByID(int TaskID){
        return taskRepository.getTaskByID(TaskID);
    }

    public Task updateTask(Task task, int memberID, float estimatedHours){
        return taskRepository.updateTask(task, memberID, estimatedHours);
    }

    public List<Task> getTasksAssignedTo(int accountID){
        return taskRepository.getTasksAssignedTo(accountID);
    }

    public Task finishTask(Task task, boolean isFinished, float realizedHours) {
        return taskRepository.finishTask(task, isFinished, realizedHours);
    }

    public List<Task> getFinishedTasks(int accountID) {
        return taskRepository.getFinishedTasks(accountID);
    }

    public void deleteTaskByID(int taskID) {
        taskRepository.deleteTaskMembers(taskID);
        taskRepository.deleteTaskHoursRealized(taskID);
        taskRepository.deleteTask(taskID);
    }

    // Check if task exceeds subproject deadline ...
    public boolean isTaskEstimatedHoursExceedingSubProjectDeadline(LocalDate taskStartDate, float estimatedHours, LocalDate subProjectDeadline) {
        int hoursPerDay = 7;
        long daysToComplete = (long) Math.ceil(estimatedHours / hoursPerDay);
        LocalDate calculatedEndDate = taskStartDate.plusDays(daysToComplete - 1);
        return calculatedEndDate.isAfter(subProjectDeadline);
    }





}
