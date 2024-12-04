package org.kea.easyscope.model;

public class TaskMember {

    private int taskMemberID; // this int is auto_incremented in the database
    private int accountID;
    private int taskID;

    public TaskMember(int taskID, int accountID, int taskMemberID) {
        this.taskID = taskID;
        this.accountID = accountID;
        this.taskMemberID = taskMemberID;
    }

    public TaskMember(){

    }

    public int getTaskMemberID() {
        return taskMemberID;
    }

    public void setTaskMemberID(int taskMemberID) {
        this.taskMemberID = taskMemberID;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
}
