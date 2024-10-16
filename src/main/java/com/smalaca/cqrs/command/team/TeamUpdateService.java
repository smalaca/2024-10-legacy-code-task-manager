package com.smalaca.cqrs.command.team;

import com.smalaca.cqrs.acl.team.AclTeamDomainModelRepository;
import com.smalaca.taskamanager.dto.TeamDto;

import java.util.Optional;

public class TeamUpdateService {
    private final TeamDomainModelRepository teamDomainModelRepository;

    public TeamUpdateService(AclTeamDomainModelRepository teamDomainModelRepository) {
        this.teamDomainModelRepository = teamDomainModelRepository;
    }

    public Optional<TeamDto> updateTeam(Long id, TeamDto teamDto) {
        Optional<TeamDomainModel> found = teamDomainModelRepository.findById(id);

        if (found.isPresent()) {
            TeamDomainModel teamDomainModel = found.get();
            teamDomainModel.updateBasicInformation(teamDto);
            teamDomainModelRepository.save(teamDomainModel);

            TeamDto dto = teamDomainModel.asDto();
            return Optional.of(dto);
        }

        return Optional.empty();
    }
}
