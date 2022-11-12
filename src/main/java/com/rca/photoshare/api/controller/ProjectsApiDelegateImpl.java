package com.rca.photoshare.api.controller;
import com.rca.photoshare.api.ProjectsApiDelegate;
import com.rca.photoshare.api.model.Project;
import com.rca.photoshare.api.database.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectsApiDelegateImpl implements ProjectsApiDelegate {

    @Autowired
    ProjectRepository projectsRepository;

    @Override
    public ResponseEntity<List<com.rca.photoshare.api.model.Project>> getProjects() {
        List<Project> projectsList = new ArrayList<>();
        projectsList.add(new Project());
        //projectsRepository.getPublicProjects();
        return ResponseEntity.ok(projectsList);
    }


}
