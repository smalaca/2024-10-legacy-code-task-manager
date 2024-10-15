package com.smalaca.parallelrun.projectmanagement;

import com.smalaca.taskamanager.model.entities.Project;
import com.smalaca.taskamanager.model.entities.Team;
import org.springframework.http.ResponseEntity;

public class ParallelRunProjectTestRecord<T> {
    private ResponseEntity<T> response;
    private Project project;
    private Team team;

    public ResponseEntity<T> getResponse() {
        return response;
    }

    public void setResponse(ResponseEntity<T> response) {
        this.response = response;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void compareWith(ParallelRunProjectTestRecord record) {
        System.out.println("RECORDS ARE THE SAME");
    }
}
