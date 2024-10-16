package com.smalaca.parallelrun.projectmanagement;

import com.smalaca.taskamanager.model.entities.Team;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
class TeamToCompare {
    private Long id;
    private Long projectId;

    static TeamToCompare createFrom(Team team) {
        TeamToCompare compare = new TeamToCompare();
        compare.id = team.getId();
        compare.projectId = team.getProject() == null ? null : team.getProject().getId();

        return compare;
    }

    static TeamToCompare createFrom(com.smalaca.projectmanagement.Team team) {
        TeamToCompare compare = new TeamToCompare();
        compare.id = team.getId();
        compare.projectId = team.getProject() == null ? null : team.getProject().getId();

        return compare;
    }
}
