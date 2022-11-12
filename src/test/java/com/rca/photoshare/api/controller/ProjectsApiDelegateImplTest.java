package com.rca.photoshare.api.controller;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

import com.rca.photoshare.api.database.ProjectRepository;
import com.rca.photoshare.api.model.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ProjectsApiDelegateImplTest {

    @Mock
    ProjectRepository projectRepositoryMock;

    @InjectMocks
    ProjectsApiDelegateImpl projectsApiDelegate;

//    @Test
    public void testGetNoProjects() {
        when(projectRepositoryMock.getPublicProjects()).thenReturn(null);
        ResponseEntity<List<Project>> response = projectsApiDelegate.getProjects();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetSomeProjects() {
//        List<Project> mockProjectList = new ArrayList<>();
//        mockProjectList.add(new Project());
//        mockProjectList.add(new Project());
//        when(projectRepositoryMock.getPublicProjects()).thenReturn(mockProjectList);

        ResponseEntity<List<Project>> response = projectsApiDelegate.getProjects();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
}
