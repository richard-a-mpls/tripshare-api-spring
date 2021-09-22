package com.rca.photoshare.api.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.rca.photoshare.api.model.Project;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, String> {

    @Query(sort="{'datestmp': -1}")
    List<Project> findByProfileId(String profileId);

    @Query(value="{ published: 'true'}", sort= "{'datestmp': -1}")
    List<Project> getPublicProjects();
}
