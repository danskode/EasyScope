package org.kea.easyscope.model;

import java.util.List;

public class Task {

    private int taskID; // this int is auto_incremented in the database
    private String taskName;
    private String taskDescription;
    private boolean taskIsFinished; // this is a TINYINT in db (1=completed, 0=incomplete)
    private int subProjectID; // this is a foreign key from sub_project (database)

    private List<TaskMember> taskMembers; // a list of task members
    private List<TaskHoursRealized> taskHoursRealized; // a list of realized hours
    private List<TaskHoursEstimated> taskHoursEstimated; // a list of estimated hours



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

    public List<TaskMember> getTaskMembers() {
        return taskMembers;
    }

    public void setTaskMembers(List<TaskMember> taskMembers) {
        this.taskMembers = taskMembers;
    }

    public List<TaskHoursRealized> getTaskHoursRealized() {
        return taskHoursRealized;
    }

    public void setTaskHoursRealized(List<TaskHoursRealized> taskHoursRealized) {
        this.taskHoursRealized = taskHoursRealized;
    }

    public List<TaskHoursEstimated> getTaskHoursEstimated() {
        return taskHoursEstimated;
    }

    public void setTaskHoursEstimated(List<TaskHoursEstimated> taskHoursEstimated) {
        this.taskHoursEstimated = taskHoursEstimated;
    }
}
