package com.smalaca.cqrs.acl.team;

import com.smalaca.cqrs.command.team.TeamDomainModel;
import com.smalaca.cqrs.command.team.TeamDomainModelRepository;
import com.smalaca.taskamanager.repository.TeamRepository;

import java.util.Optional;

public class AclTeamDomainModelRepository implements TeamDomainModelRepository {
    private final TeamRepository teamRepository;

    public AclTeamDomainModelRepository(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public void save(TeamDomainModel teamDomainModel) {
        teamRepository.save(teamDomainModel.getTeam());
    }

    @Override
    public Optional<TeamDomainModel> findById(Long id) {
        return teamRepository.findById(id).map(TeamDomainModel::new);
    }
}
