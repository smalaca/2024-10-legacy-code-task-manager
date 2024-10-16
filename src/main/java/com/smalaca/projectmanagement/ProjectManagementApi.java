package com.smalaca.projectmanagement;

import com.smalaca.parallelrun.projectmanagement.ParallelRunProjectTestRecord;
import com.smalaca.taskamanager.dto.ProjectDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

public class ProjectManagementApi {
    private final ProjectRepository projectRepository;

    public ProjectManagementApi(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ParallelRunProjectTestRecord createProject(ProjectDto projectDto, UriComponentsBuilder uriComponentsBuilder) {
        ParallelRunProjectTestRecord record = new ParallelRunProjectTestRecord();
        if (exists(projectDto)) {
            ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.CONFLICT);
            record.setResponse(response);
            return record;
        } else {
            Project project = new Project();
            project.setName(projectDto.getName());

            record.setProject(project);
            Project saved = projectRepository.save(project);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriComponentsBuilder.path("/project/{id}").buildAndExpand(saved.getId()).toUri());

            ResponseEntity<Void> response = new ResponseEntity<>(headers, HttpStatus.CREATED);
            record.setResponse(response);
            return record;
        }
    }

    private boolean exists(ProjectDto projectDto) {
        return !projectRepository.findByName(projectDto.getName()).isEmpty();
    }
}
