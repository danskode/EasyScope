package org.kea.easyscope.model;

public class TaskHoursRealized {

    private int taskHoursRealizedID; // this int is auto_incremented in the database
    private float taskHoursRealized;

    private int taskID; // foreign key to task
    private int accountID; // foreign key to account

    public TaskHoursRealized(int taskHoursRealizedID, float taskHoursRealized, int taskID, int accountID) {
        this.taskHoursRealizedID = taskHoursRealizedID;
        this.taskHoursRealized = taskHoursRealized;
        this.taskID = taskID;
        this.accountID = accountID;
    }

    public TaskHoursRealized() {

    }

    public int getTaskHoursRealizedID() {
        return taskHoursRealizedID;
    }

    public void setTaskHoursRealizedID(int taskHoursRealizedID) {
        this.taskHoursRealizedID = taskHoursRealizedID;
    }

    public float getTaskHoursRealized() {
        return taskHoursRealized;
    }

    public void setTaskHoursRealized(float taskHoursRealized) {
        this.taskHoursRealized = taskHoursRealized;
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
