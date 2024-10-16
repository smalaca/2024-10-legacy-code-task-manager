package com.smalaca.parallelrun.projectmanagement;

import com.smalaca.taskamanager.model.entities.Project;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

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
//        compare.projectStatus = project.getProjectStatus().name();
//        compare.productOwnerId = project.getProductOwner() == null ? null : project.getProductOwner().getId();
//        compare.teams = project.getTeams().stream().map(Team::getId).collect(toList());

        return compare;
    }

    static ProjectToCompare createFrom(com.smalaca.projectmanagement.Project project) {
        ProjectToCompare compare = new ProjectToCompare();
        compare.id = project.getId();
        compare.name = project.getName();
//        compare.projectStatus = project.getProjectStatus().name();
//        compare.productOwnerId = project.getProductOwner() == null ? null : project.getProductOwner().getId();
//        compare.teams = project.getTeams().stream().map(Team::getId).collect(toList());

        return compare;
    }

    boolean isEqualWithoutId(ProjectToCompare compare) {
        return areEqual(name, compare.name)
                && areEqual(projectStatus, compare.projectStatus)
                && areEqual(productOwnerId, compare.productOwnerId)
                && teams.equals(compare.teams);
    }

    private boolean areEqual(String value, String toCompare) {
        return (value == null && toCompare == null) || value.equals(toCompare);
    }

    private boolean areEqual(Long value, Long toCompare) {
        return (value == null && toCompare == null) || value.equals(toCompare);
    }
}
