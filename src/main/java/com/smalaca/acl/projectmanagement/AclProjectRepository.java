package com.smalaca.acl.projectmanagement;

import com.smalaca.parallelrun.projectmanagement.ParallelRunProjectInteraction;
import com.smalaca.projectmanagement.ProductOwner;
import com.smalaca.projectmanagement.Project;
import com.smalaca.projectmanagement.ProjectRepository;
import com.smalaca.projectmanagement.ProjectStatus;
import com.smalaca.taskamanager.model.entities.Team;

import java.util.Optional;

public class AclProjectRepository implements ProjectRepository {
    private final com.smalaca.taskamanager.repository.ProjectRepository projectRepository;
    private final ParallelRunProjectInteraction interaction;
    private final boolean enableNewCode;

    public AclProjectRepository(com.smalaca.taskamanager.repository.ProjectRepository projectRepository, ParallelRunProjectInteraction interaction, boolean enableNewCode) {
        this.projectRepository = projectRepository;
        this.interaction = interaction;
        this.enableNewCode = enableNewCode;
    }

    @Override
    public Optional<Project> findByName(String name) {
        if (enableNewCode) {
            return projectRepository.findByName(name).map(this::asProject);
        } else {
            return interaction.findProjectByName(name);
        }
    }

    @Override
    public Project save(Project project) {
        if (enableNewCode) {
            com.smalaca.taskamanager.model.entities.Project saved = projectRepository.save(asProject(project));
            return saved == null ? null : asProject(saved);
        } else {
            return project;
        }
    }

    @Override
    public Optional<Project> findById(Long id) {
        if (enableNewCode) {
            return projectRepository.findById(id).map(this::asProject);
        } else {
            return interaction.findProjectById(id);
        }
    }

    @Override
    public void delete(Project project) {
        if (enableNewCode) {
            projectRepository.delete(asProject(project));
        }
    }

    private com.smalaca.projectmanagement.Project asProject(com.smalaca.taskamanager.model.entities.Project project) {
        com.smalaca.projectmanagement.Project found = new com.smalaca.projectmanagement.Project();
        found.setId(project.getId());
        found.setProjectStatus(ProjectStatus.valueOf(project.getProjectStatus().name()));
        found.setName(project.getName());
        if (project.getProductOwner() != null) {
            ProductOwner productOwner = new ProductOwner();
            productOwner.setId(project.getProductOwner().getId());
            found.setProductOwner(productOwner);
        }

        if (project.getTeams() != null) {
            project.getTeams().forEach(team -> {
                com.smalaca.projectmanagement.Team newTeam = new com.smalaca.projectmanagement.Team();
                newTeam.setProject(found);
                newTeam.setId(team.getId());
                found.addTeam(newTeam);
            });
        }

        return found;
    }

    private com.smalaca.taskamanager.model.entities.Project asProject(com.smalaca.projectmanagement.Project project) {
        com.smalaca.taskamanager.model.entities.Project found = new com.smalaca.taskamanager.model.entities.Project();
        found.setId(project.getId());
        found.setProjectStatus(com.smalaca.taskamanager.model.enums.ProjectStatus.valueOf(project.getProjectStatus().name()));
        found.setName(project.getName());
        if (project.getProductOwner() != null) {
            com.smalaca.taskamanager.model.entities.ProductOwner productOwner = new com.smalaca.taskamanager.model.entities.ProductOwner();
            productOwner.setId(project.getProductOwner().getId());
            found.setProductOwner(productOwner);
        }

        if (project.getTeams() != null) {
            project.getTeams().forEach(team -> {
                Team newTeam = new Team();
                newTeam.setProject(found);
                newTeam.setId(team.getId());
                found.addTeam(newTeam);
            });
        }

        return found;
    }
}
