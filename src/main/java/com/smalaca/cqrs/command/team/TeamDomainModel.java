package com.smalaca.cqrs.command.team;

import com.smalaca.taskamanager.dto.TeamDto;
import com.smalaca.taskamanager.model.embedded.Codename;
import com.smalaca.taskamanager.model.entities.Team;

public class TeamDomainModel {
    private final Team team;

    public TeamDomainModel(Team team) {
        this.team = team;
    }

    void updateBasicInformation(TeamDto teamDto) {
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
    }

    public Team getTeam() {
        return team;
    }

    TeamDto asDto() {
        TeamDto dto = new TeamDto();
        dto.setId(team.getId());
        dto.setName(team.getName());
        if (team.getCodename() != null) {
            dto.setCodenameShort(team.getCodename().getShortName());
            dto.setCodenameFull(team.getCodename().getFullName());
        }

        dto.setDescription(team.getDescription());
        
        return dto;
    }
}
