package com.rca.photoshare.api.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.rca.photoshare.api.model.Project;

import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, String> {
    List<Project> findByProfileId(String profileId);
}