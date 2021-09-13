package com.rca.photoshare.api.controller;
import com.rca.photoshare.api.ProjectsApiDelegate;
import com.rca.photoshare.api.authorization.TokenModel;
import com.rca.photoshare.api.model.Project;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ProjectsApiDelegateImpl implements ProjectsApiDelegate {
    @Override
    public ResponseEntity<List<Project>> getProjects() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("From /Projects: " + ((TokenModel)authentication.getPrincipal()).getSubject());
        List<Project> projectsList = new ArrayList<Project>();
        projectsList.add(new Project());
        projectsList.add(new Project());
        projectsList.add(new Project());
        return ResponseEntity.ok(projectsList);
    }
}
