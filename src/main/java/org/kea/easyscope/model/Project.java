package org.kea.easyscope.model;

public class Project {

    private int projectID; // this int is auto_incremented in the database
    private String projectName;
    private String projectDescription;
    private boolean isFinished; // this is a TINYINT in db (1=completed, 0=incomplete)

    private int accountID; // This is a foreign key from account (database)


    public Project(String projectName, String projectDescription, int accountID) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.isFinished = false; // A new project is not finished as standard
        this.accountID = accountID;
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

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }


}
