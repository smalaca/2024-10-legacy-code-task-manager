package com.smalaca.acl.projectmanagement;

import com.smalaca.projectmanagement.Project;
import com.smalaca.projectmanagement.ProjectRepository;

import java.util.Optional;

public class FakeAclProjectRepository implements ProjectRepository {
    @Override
    public Optional<Project> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public Project save(Project project) {
        return project;
    }
}
