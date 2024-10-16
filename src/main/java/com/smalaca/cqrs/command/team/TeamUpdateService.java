package com.smalaca.cqrs.command.team;

import com.smalaca.taskamanager.dto.TeamDto;
import com.smalaca.taskamanager.model.embedded.Codename;
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
            Team team = found.get();

            if (teamDto.getName() != null) {
                team.setName(teamDto.getName());
            }

            if (teamDto.getCodenameShort() != null && teamDto.getCodenameFull() != null) {
                Codename codename = new Codename();
                codename.setShortName(teamDto.getCodenameShort());
                codename.setFullName(teamDto.getCodenameFull());
                team.setCodename(codename);
            }

            if (teamDto.getDescription() != null) {
                team.setDescription(teamDto.getDescription());
            }

            Team updated = teamRepository.save(team);

            TeamDto dto = new TeamDto();
            dto.setId(updated.getId());
            dto.setName(updated.getName());
            if (updated.getCodename() != null) {
                dto.setCodenameShort(updated.getCodename().getShortName());
                dto.setCodenameFull(updated.getCodename().getFullName());
            }

            dto.setDescription(updated.getDescription());

            return Optional.of(dto);
        }

        return Optional.empty();
    }
}
