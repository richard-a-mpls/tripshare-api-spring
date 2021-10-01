package com.rca.photoshare.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rca.photoshare.api.MeApiDelegate;
import com.rca.photoshare.api.authorization.TokenModel;
import com.rca.photoshare.api.database.ProjectRepository;
import com.rca.photoshare.api.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map.Entry;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MeApiDelegateImpl implements MeApiDelegate {

    @Autowired
    ProjectRepository projectsRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ResponseEntity<Project> addProject(Project project) {
        project.setProfileId(getAuthenticatedProfileId());
        project.setPublished(false);
        project.setPhotoArray(new ArrayList<>());
        if (project.getDatestmp() == null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            project.setDatestmp(dateFormat.format(new Date(System.currentTimeMillis())));
        }

        Project createdProject = projectsRepository.insert(project);
        return ResponseEntity.ok(createdProject);
    }

    @Override
    public ResponseEntity<List<Project>> getSessionProjects() {
        List<Project> projectList = projectsRepository.findByProfileId(getAuthenticatedProfileId());
        return ResponseEntity.ok(projectList);
    }

    @Override
    public ResponseEntity<Project> getSessionProjectById(String projectId) {
        Optional<Project> foundProject = projectsRepository.findById(projectId);
        if (foundProject.isPresent()) {
            if (!verifyProfileId(foundProject.get().getProfileId())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            return ResponseEntity.ok(foundProject.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @Override
    public ResponseEntity<Project> patchProject(String projectId, Project project) {
        Optional<Project> foundProject = projectsRepository.findById(projectId);
        if (foundProject.isPresent()) {
            if (!verifyProfileId(foundProject.get().getProfileId())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            // I would have thought there would be a better way to do a partial document update but seemingly
            // without this extra work, any fields not provided get set to null.  will keep an eye out for
            // better alternatives, if none, this code can be made generic and moved somewhere common for reuse.
            Update update = new Update();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.convertValue(project, JsonNode.class);
            Iterator<Entry<String, JsonNode>> iterator = node.fields();
            while (iterator.hasNext()) {
                Entry<String, JsonNode> entry = iterator.next();
                if (!entry.getValue().isNull()) {
                    update.set(entry.getKey(), entry.getValue().asText());
                }
            }
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(projectId));

            mongoTemplate.findAndModify(query, update, Project.class);
            Optional<Project> returnProject = projectsRepository.findById(projectId);
            if (returnProject.isPresent()) {
                return ResponseEntity.ok(returnProject.get());
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @Override
    public ResponseEntity<Void> deleteProject(String projectId) {
        Optional<Project> deleteProject = projectsRepository.findById(projectId);
        if (deleteProject.isPresent()) {
            if (verifyProfileId(deleteProject.get().getProfileId())) {
                projectsRepository.delete(deleteProject.get());
                return ResponseEntity.ok(null);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    private boolean verifyProfileId(String profileId) {
        return profileId.equals(getAuthenticatedProfileId());
    }

    private String getAuthenticatedProfileId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((TokenModel) authentication.getPrincipal()).getSubject();
    }
}
