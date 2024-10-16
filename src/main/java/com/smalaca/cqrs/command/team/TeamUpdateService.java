package com.smalaca.cqrs.command.team;

import com.smalaca.taskamanager.dto.TeamDto;
import com.smalaca.taskamanager.model.entities.Team;
import com.smalaca.taskamanager.repository.TeamRepository;

import java.util.Optional;

public class TeamUpdateService {
    private final TeamRepository teamRepository;

    public TeamUpdateService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Optional<TeamDto> updateTeam(Long id, TeamDto teamDto) {
        Optional<Team> found = teamRepository.findById(id);

        if (found.isPresent()) {
            TeamDomainModel teamDomainModel = new TeamDomainModel(found.get());
            teamDomainModel.updateBasicInformation(teamDto);

            teamRepository.save(teamDomainModel.getTeam());
            TeamDto dto = teamDomainModel.asDto();

            return Optional.of(dto);
        }

        return Optional.empty();
    }
}
