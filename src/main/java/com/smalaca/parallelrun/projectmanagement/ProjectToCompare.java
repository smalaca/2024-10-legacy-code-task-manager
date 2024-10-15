package com.smalaca.parallelrun.projectmanagement;

import com.smalaca.taskamanager.model.entities.Project;
import com.smalaca.taskamanager.model.entities.Team;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@EqualsAndHashCode
class ProjectToCompare {
    private Long id;
    private String name;
    private String projectStatus;
    private Long productOwnerId;
    private List<Long> teams = new ArrayList<>();

    static ProjectToCompare createFrom(Project project) {
        ProjectToCompare compare = new ProjectToCompare();
        compare.id = project.getId();
        compare.name = project.getName();
        compare.projectStatus = project.getProjectStatus().name();
        compare.productOwnerId = project.getProductOwner() == null ? null : project.getProductOwner().getId();
        compare.teams = project.getTeams().stream().map(Team::getId).collect(toList());

        return compare;
    }
}
