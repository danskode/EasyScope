package org.kea.easyscope.model;

import java.time.LocalDate;

public class SubProject {

    private int subProjectID; // this int is auto_incremented in the database
    private String subProjectName;
    private String subProjectDescription;
    private LocalDate subProjectDeadline;
    private boolean isFinished; // this is a TINYINT in db (1=completed, 0=incomplete)

    private float totalEstimatedHours;
    private float totalRealizedHours;

    private String deadlineNotice;

    private int projectID; // This is a foreign key from project (database)

    public SubProject(String subProjectName, String subProjectDescription, LocalDate subProjectDeadline, float totalEstimatedHours, float totalRealizedHours, int projectID) {
        this.subProjectName = subProjectName;
        this.subProjectDescription = subProjectDescription;
        this.subProjectDeadline = subProjectDeadline;
        this.totalEstimatedHours = totalEstimatedHours; // join from database
        this.totalRealizedHours = totalRealizedHours; // join from database
        this.isFinished = false;
        this.projectID = projectID;
    }

    public SubProject(String subProjectName, String subProjectDescription, LocalDate subProjectDeadline, int projectID) {
        this.subProjectName = subProjectName;
        this.subProjectDescription = subProjectDescription;
        this.subProjectDeadline = subProjectDeadline;
        this.isFinished = false;
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

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public float getTotalEstimatedHours() {
        return totalEstimatedHours;
    }
    public void setTotalEstimatedHours(float totalEstimatedHours) {
        this.totalEstimatedHours = totalEstimatedHours;
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

    public void setTotalRealizedHours(Float totalRealizedHours) {
        this.totalRealizedHours = totalRealizedHours;
    }
    public float getTotalRealizedHours() {
        return totalRealizedHours;
    }
    public void setDeadlineNotice(String deadlineNotice) {
        this.deadlineNotice = deadlineNotice;
    }
    public String getDeadlineNotice() {
        return deadlineNotice;
    }

}
