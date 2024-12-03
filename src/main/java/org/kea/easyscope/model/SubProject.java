package org.kea.easyscope.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class SubProject {

    private int subProjectID;
    private String subProjectName;
    private String subProjectDescription;
    private LocalDate subProjectDeadline;
    private boolean isActive;
    private int projectID; // This is a foreign key in the database

    public SubProject(String subProjectName, String subProjectDescription, LocalDate subProjectDeadline, boolean isActive, int projectID) {
        this.subProjectName = subProjectName;
        this.subProjectDescription = subProjectDescription;
        this.subProjectDeadline = subProjectDeadline;
        this.isActive = isActive;
        this.projectID = projectID;
    }


    public SubProject() {
    }

    public int getSubProjectID() {
        return subProjectID;
    }

    public void setSubProjectID(int subProjectID) {
        this.subProjectID = subProjectID;
    }

    public String getSubProjectName() {
        return subProjectName;
    }

    public void setSubProjectName(String subProjectName) {
        this.subProjectName = subProjectName;
    }

    public String getSubProjectDescription() {
        return subProjectDescription;
    }

    public void setSubProjectDescription(String subProjectDescription) {
        this.subProjectDescription = subProjectDescription;
    }

    public LocalDate getSubProjectDeadline() {
        return subProjectDeadline;
    }

    public void setSubProjectDeadline(LocalDate subProjectDeadline) {
        this.subProjectDeadline = subProjectDeadline;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    @Override
    public String toString() {
        return "SubProject{" +
                "subProjectID=" + subProjectID +
                ", subProjectName='" + subProjectName + '\'' +
                ", subProjectDescription='" + subProjectDescription + '\'' +
                ", subProjectDeadline=" + subProjectDeadline +
                '}';
    }
}
