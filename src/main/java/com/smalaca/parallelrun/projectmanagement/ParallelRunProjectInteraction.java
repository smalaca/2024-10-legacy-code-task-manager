package com.smalaca.parallelrun.projectmanagement;

import com.smalaca.projectmanagement.ProductOwner;
import com.smalaca.projectmanagement.ProjectStatus;
import com.smalaca.taskamanager.model.entities.Project;
import com.smalaca.taskamanager.model.entities.Team;

import java.util.Optional;

public class ParallelRunProjectInteraction {
    private String projectName;
    private Optional<com.smalaca.projectmanagement.Project> resultByName;
    private Long projectId;
    private Optional<com.smalaca.projectmanagement.Project> resultById;
    private Long teamId;
    private Optional<com.smalaca.projectmanagement.Team> teamById;

    public void registerExistTeamById(Long teamId, Optional<Team> teamById) {
        this.teamId = teamId;
        this.teamById = teamById.map(this::asTeam);
    }

    public void registerExistProjectById(Long projectId, Optional<Project> resultById) {
        this.projectId = projectId;
        this.resultById = resultById.map(this::asProject);
    }

    public void registerExistProjectByName(String name, Optional<Project> resultByName) {
        this.projectName = name;
        this.resultByName = resultByName.map(this::asProject);
    }

    public Optional<com.smalaca.projectmanagement.Project> findProjectById(Long id) {
        if (id.equals(this.projectId)) {
            return resultById;
        } else {
            throw new RuntimeException("SOMETHING WENT WRONG");
        }
    }

    public Optional<com.smalaca.projectmanagement.Project> findProjectByName(String name) {
        if (name.equals(this.projectName)) {
            return resultByName;
        } else {
            throw new RuntimeException("SOMETHING WENT WRONG");
        }
    }

    private com.smalaca.projectmanagement.Project asProject(Project project) {
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

    public Optional<com.smalaca.projectmanagement.Team> findTeamById(Long id) {
        if (id.equals(this.teamId)) {
            return teamById;
        }
        throw new RuntimeException("SOMETHING WENT WRONG");
    }

    private com.smalaca.projectmanagement.Team asTeam(Team team) {
        com.smalaca.projectmanagement.Team found = new com.smalaca.projectmanagement.Team();
        found.setId(team.getId());
        Project projectLegacy = team.getProject();

        if (projectLegacy != null) {
            com.smalaca.projectmanagement.Project project = new com.smalaca.projectmanagement.Project();
            project.setId(projectLegacy.getId());
            found.setProject(project);
        }

        return found;
    }
}
