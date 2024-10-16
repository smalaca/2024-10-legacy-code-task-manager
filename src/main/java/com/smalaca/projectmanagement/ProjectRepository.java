package com.smalaca.projectmanagement;

import java.util.Optional;

public interface ProjectRepository {
    Optional<Project> findByName(String name);

    Project save(Project project);

    Optional<Project> findById(Long id);
}
