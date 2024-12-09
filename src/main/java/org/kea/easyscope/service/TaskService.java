package org.kea.easyscope.service;

import org.kea.easyscope.model.Task;
import org.kea.easyscope.repository.TaskRepository;
import org.springframework.stereotype.Service;

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
        return taskRepository.getTaskById(TaskID);
    }

    public Task updateTask(Task task, int memberID, float estimatedHours){
        return taskRepository.updateTask(task, memberID, estimatedHours);
    }





}
