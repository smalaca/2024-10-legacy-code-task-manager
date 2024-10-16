package com.smalaca.projectmanagement;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import static com.smalaca.projectmanagement.ProjectStatus.IDEA;

@Entity
public class Project {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private ProjectStatus projectStatus = IDEA;

    @ManyToOne
    private ProductOwner productOwner;

    @OneToMany
    private List<Team> teams = new ArrayList<>();

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public ProductOwner getProductOwner() {
        return productOwner;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void addTeam(Team team) {
        if (!teams.contains(team)) {
            teams.add(team);
        }
    }

    void removeTeam(Team team) {
        if (!teams.contains(team)) {
            throw new RuntimeException();
        }
        teams.remove(team);
    }
}
