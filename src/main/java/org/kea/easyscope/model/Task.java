package org.kea.easyscope.model;

import org.springframework.data.annotation.Transient;
import java.time.LocalDate;


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

    private Account assignedTo;

    // Give task a start date
    private LocalDate taskStartDate;

    // Because it is not needed in databse, we use this annotation to use the entity on the fly when needed (hybrid approach) ...
    @Transient
    private LocalDate taskEndDate;
    public LocalDate getTaskEndDate() {
        if (taskStartDate != null && estimatedHours > 0) {
            return taskStartDate.plusDays((long) Math.ceil((double) estimatedHours / 7));
        }
        return null;
    }

    public Task(String taskName, String taskDescription, int subProjectID, float estimatedHours, float realizedHours, LocalDate taskStartDate) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.subProjectID = subProjectID;
        this.estimatedHours = estimatedHours;
        this.realizedHours = realizedHours;
        this.taskIsFinished = false; // a task is not finished when it just got created
        this.taskStartDate = taskStartDate;
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

    public Account getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Account assignedTo) {
        this.assignedTo = assignedTo;
    }

    // Getter and setter for taskStartDate ...
    public LocalDate getTaskStartDate() {return taskStartDate;}
    public void setTaskStartDate(LocalDate taskStartDate) {this.taskStartDate = taskStartDate;}

    @Override
    public String toString() {
        return "Task{" +
                "taskID=" + taskID +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskIsFinished=" + taskIsFinished +
                ", subProjectID=" + subProjectID +
                ", estimatedHours=" + estimatedHours +
                '}';
    }
}
