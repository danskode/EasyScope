package org.kea.easyscope.model;

import java.util.List;

public class Task {

    private int taskID; // this int is auto_incremented in the database
    private String taskName;
    private String taskDescription;
    private boolean taskIsFinished; // this is a TINYINT in db (1=completed, 0=incomplete)
    private int subProjectID; // this is a foreign key from sub_project (database)

    // Represents estimated and realized hours for a task,
    // fetched via JOIN queries for simplicity and normalized data handling.
    private TaskHoursEstimated estimatedHours;
    private TaskHoursRealized realizedHours;




    public Task(String taskName, String taskDescription, int subProjectID){
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.subProjectID = subProjectID;
        this.taskIsFinished = false; // a task is not finished when it just got created
    }

    public Task(){

    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public boolean isTaskIsFinished() {
        return taskIsFinished;
    }

    public void setTaskIsFinished(boolean taskIsFinished) {
        this.taskIsFinished = taskIsFinished;
    }

    public int getSubProjectID() {
        return subProjectID;
    }

    public void setSubProjectID(int subProjectID) {
        this.subProjectID = subProjectID;
    }


}
