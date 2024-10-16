package com.smalaca.parallelrun.projectmanagement;

import com.smalaca.taskamanager.model.entities.Project;
import com.smalaca.taskamanager.model.entities.Team;
import org.springframework.http.ResponseEntity;

public class ParallelRunProjectTestRecord<T> {
    private ResponseEntity<T> response;
    private ProjectToCompare project;
    private TeamToCompare team;

    public ResponseEntity<T> getResponse() {
        return response;
    }

    public void setResponse(ResponseEntity<T> response) {
        this.response = response;
    }

    public void setProject(Project project) {
        this.project = ProjectToCompare.createFrom(project);
    }

    public void setProject(com.smalaca.projectmanagement.Project project) {
        this.project = ProjectToCompare.createFrom(project);
    }

    public void setTeam(Team team) {
        this.team = TeamToCompare.createFrom(team);
    }

    public void compareWith(ParallelRunProjectTestRecord record) {
        if (isRequestEqual(record) && isProjectEqual(record) && isTeamEqual(record)) {
            System.out.println("RECORDS ARE THE SAME");
        } else {
            System.out.println("RECORDS ARE NOT THE SAME");
        }
    }

    public void compareWithoutId(ParallelRunProjectTestRecord record) {
        if (isRequestEqual(record) && isProjectEqualWithoutId(record) && isTeamEqual(record)) {
            System.out.println("RECORDS ARE THE SAME");
        } else {
            System.out.println("RECORDS ARE NOT THE SAME");
        }
    }

    private boolean isTeamEqual(ParallelRunProjectTestRecord record) {
        return (record.team == null && this.team == null) || record.team.equals(this.team);
    }

    private boolean isProjectEqualWithoutId(ParallelRunProjectTestRecord record) {
        return record.project.isEqualWithoutId(this.project);
    }

    private boolean isProjectEqual(ParallelRunProjectTestRecord record) {
        return record.project.equals(this.project);
    }

    private boolean isRequestEqual(ParallelRunProjectTestRecord record) {
        return record.response.getStatusCode().equals(this.response.getStatusCode());
    }
}
