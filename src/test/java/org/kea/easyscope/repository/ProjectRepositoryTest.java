package org.kea.easyscope.repository;

import org.junit.jupiter.api.Test;
import org.kea.easyscope.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

// integration test
@SpringBootTest(properties = "spring.profiles.active=test")
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void getProjectByProjectID_shouldReturnProject() {
        // arrange: set up a project id to retrieve from the database
        int projectID = 2;

        // act: retrieve the project from the database using getProjectByProjectID
        Project retrievedProject = projectRepository.getProjectByProjectID(projectID);

        // assert: verify that the project is retrieved correctly
        assertNotNull(retrievedProject, "the project should not be null.");
        assertEquals(projectID, retrievedProject.getProjectID(), "the project id should match.");
        assertEquals("Tesla", retrievedProject.getProjectName(), "the project name should match.");
        assertEquals("The client wants an app for more republican votes", retrievedProject.getProjectDescription(), "the project description should match.");
        assertFalse(retrievedProject.isFinished(), "the project should not be finished.");
    }
}