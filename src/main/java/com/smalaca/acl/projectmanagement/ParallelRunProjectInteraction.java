package com.smalaca.acl.projectmanagement;

import com.smalaca.taskamanager.model.entities.Project;

import java.util.Optional;

public class ParallelRunProjectInteraction {
    private String projectName;
    private Optional<Project> resultByName;

    public void registerExistProjectByName(String name, Optional<Project> result) {
        this.projectName = name;
        this.resultByName = result;
    }

    Optional<com.smalaca.projectmanagement.Project> findProjectByName(String name) {
        if (name.equals(this.projectName)) {
            return resultByName.map(project -> {
                com.smalaca.projectmanagement.Project found = new com.smalaca.projectmanagement.Project();
                found.setId(project.getId());
                return found;
            });
        } else {
            throw new RuntimeException("SOMETHING WENT WRONG");
        }
    }
}
