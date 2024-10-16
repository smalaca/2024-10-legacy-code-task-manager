package com.smalaca.acl.projectmanagement;

import com.smalaca.parallelrun.projectmanagement.ParallelRunProjectInteraction;
import com.smalaca.projectmanagement.Project;
import com.smalaca.projectmanagement.ProjectRepository;

import java.util.Optional;

public class FakeAclProjectRepository implements ProjectRepository {
    private final ParallelRunProjectInteraction interaction;

    public FakeAclProjectRepository(ParallelRunProjectInteraction interaction) {
        this.interaction = interaction;
    }

    @Override
    public Optional<Project> findByName(String name) {
        return interaction.findProjectByName(name);
    }

    @Override
    public Project save(Project project) {
        return project;
    }

    @Override
    public Optional<Project> findById(Long id) {
        return interaction.findProjectById(id);
    }

    @Override
    public void delete(Project project) {

    }
}
