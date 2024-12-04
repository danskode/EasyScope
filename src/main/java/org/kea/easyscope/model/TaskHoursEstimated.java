package org.kea.easyscope.model;

public class TaskHoursEstimated {

    private int taskHoursEstimatedID; // this int is auto_incremented in the database
    private float taskHoursEstimated;

    private int taskID; // foreign key to task
    private int accountID; // foreign key to account

    public TaskHoursEstimated(int taskHoursEstimatedID, float taskHoursEstimated, int taskID, int accountID) {
        this.taskHoursEstimatedID = taskHoursEstimatedID;
        this.taskHoursEstimated = taskHoursEstimated;
        this.taskID = taskID;
        this.accountID = accountID;
    }

    public TaskHoursEstimated() {

    }

    public int getTaskHoursEstimatedID() {
        return taskHoursEstimatedID;
    }

    public void setTaskHoursEstimatedID(int taskHoursEstimatedID) {
        this.taskHoursEstimatedID = taskHoursEstimatedID;
    }

    public float getTaskHoursEstimated() {
        return taskHoursEstimated;
    }

    public void setTaskHoursEstimated(float taskHoursEstimated) {
        this.taskHoursEstimated = taskHoursEstimated;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }
}
