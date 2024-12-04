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
    private float estimatedHours;
    private float realizedHours;




    public Task(String taskName, String taskDescription, int subProjectID, float estimatedHours, float realizedHours){
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.subProjectID = subProjectID;
        this.estimatedHours = estimatedHours;
        this.realizedHours = realizedHours;
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

    public float getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(float estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public float getRealizedHours() {
        return realizedHours;
    }

    public void setRealizedHours(float realizedHours) {
        this.realizedHours = realizedHours;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskID=" + taskID +
                ", taskName='" + taskName + '\'' +
                ", description='" + taskDescription + '\'' +
                ", subProjectID=" + subProjectID +
                '}';
    }

}
