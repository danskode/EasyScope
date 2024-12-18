package org.kea.easyscope.service;

import org.junit.jupiter.api.Test;
import org.kea.easyscope.model.Project;
import org.kea.easyscope.repository.ProjectRepository;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// this is a unit test
class ProjectServiceTest {

    @Test
    void getProjectFromAccountID() {
        // arrange: create a mock repository
        ProjectRepository repo = Mockito.mock(ProjectRepository.class);

        // create a mock list of projects
        Project project1 = new Project();
        project1.setProjectID(1);
        project1.setProjectName("Tesla");
        project1.setProjectDescription("Description Tesla Test");
        project1.setFinished(false);
        project1.setAccountID(1);

        Project project2 = new Project();
        project2.setProjectID(2);
        project2.setProjectName("Audi");
        project2.setProjectDescription("Description Audi Test");
        project2.setFinished(true);
        project2.setAccountID(1);

        List<Project> mockProjects = Arrays.asList(project1, project2);

        // mock the repository method
        Mockito.when(repo.getProjectFromAccountID(1)).thenReturn(mockProjects);

        // inject mock repository into service
        ProjectService underTest = new ProjectService(repo);

        // act: get the list of projects for accountID 1
        List<Project> actualProjects = underTest.getProjectFromAccountID(1);

        // assert: verify the list of projects is not null and has the correct size
        assertNotNull(actualProjects);
        assertEquals(2, actualProjects.size());
        assertEquals("Tesla", actualProjects.get(0).getProjectName());
        assertEquals("Audi", actualProjects.get(1).getProjectName());

        // console out print
        System.out.println("This should be 2" + actualProjects.size());
        System.out.println("This should be Tesla: " + actualProjects.get(0).getProjectName());
        System.out.println("This should be Audi: " + actualProjects.get(1).getProjectName());
    }
}