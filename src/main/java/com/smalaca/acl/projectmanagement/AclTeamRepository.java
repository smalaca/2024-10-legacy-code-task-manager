package com.smalaca.acl.projectmanagement;

import com.smalaca.parallelrun.projectmanagement.ParallelRunProjectInteraction;
import com.smalaca.projectmanagement.Team;
import com.smalaca.taskamanager.model.entities.Project;
import com.smalaca.taskamanager.repository.TeamRepository;

import java.util.Optional;

public class AclTeamRepository implements com.smalaca.projectmanagement.TeamRepository {
    private final ParallelRunProjectInteraction interaction;
    private final boolean enableNewCode;
    private final TeamRepository teamRepository;

    public AclTeamRepository(TeamRepository teamRepository, ParallelRunProjectInteraction interaction, boolean enableNewCode) {
        this.interaction = interaction;
        this.enableNewCode = enableNewCode;
        this.teamRepository = teamRepository;
    }

    @Override
    public Optional<Team> findById(Long id) {
        if (enableNewCode) {
            return teamRepository.findById(id).map(this::asTeam);
        } else {
            return interaction.findTeamById(id);
        }
    }

    @Override
    public void save(Team team) {
        if (enableNewCode) {
            teamRepository.save(asTeam(team));
        }
    }

    private com.smalaca.taskamanager.model.entities.Team asTeam(com.smalaca.projectmanagement.Team team) {
        com.smalaca.taskamanager.model.entities.Team found = new com.smalaca.taskamanager.model.entities.Team();
        found.setId(team.getId());
        com.smalaca.projectmanagement.Project projectLegacy = team.getProject();

        if (projectLegacy != null) {
            Project project = new Project();
            project.setId(projectLegacy.getId());
            found.setProject(project);
        }

        return found;
    }

    private com.smalaca.projectmanagement.Team asTeam(com.smalaca.taskamanager.model.entities.Team team) {
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
