package org.kea.easyscope.model;

import java.util.ArrayList;

public class Project {

    private int projectID;
    private String projectName;
    private String projectDescription;
    private boolean isActive;

    private int accountID; // This is a foreign key in the database
    //private List<SubProject> subProjects;


    public Project(String projectName, String projectDescription, boolean isActive, int accountID) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.isActive = isActive;
        this.accountID = accountID;
       // this.subProjects = new ArrayList<>(); // vil starte med en tom liste. ved ikke om det skal være her. vent slut..
    }


    public Project() {
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

   /* public List<SubProject> getSubProjects() {
        return subProjects;
    }

    public void setSubProjects(List<SubProject> subProjects) {
        this.subProjects = subProjects;
    }*/
}
