package com.smalaca.projectmanagement;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
}
