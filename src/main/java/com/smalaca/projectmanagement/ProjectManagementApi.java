package com.smalaca.projectmanagement;

import com.smalaca.parallelrun.projectmanagement.ParallelRunProjectTestRecord;
import com.smalaca.taskamanager.dto.ProjectDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

public class ProjectManagementApi {
    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;

    public ProjectManagementApi(ProjectRepository projectRepository, TeamRepository teamRepository) {
        this.projectRepository = projectRepository;
        this.teamRepository = teamRepository;
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

    public ParallelRunProjectTestRecord updateProject(Long id, ProjectDto projectDto) {
        ParallelRunProjectTestRecord<ProjectDto> record = new ParallelRunProjectTestRecord<>();
        try {
            Project project = getProjectById(id);
            project.setProjectStatus(ProjectStatus.valueOf(projectDto.getProjectStatus()));

            Project updated = projectRepository.save(project);
            record.setProject(updated);

            ProjectDto response = new ProjectDto();
            response.setId(updated.getId());
            response.setName(updated.getName());
            response.setProjectStatus(updated.getProjectStatus().name());

            if (updated.getProductOwner() != null) {
                response.setProductOwnerId(updated.getProductOwner().getId());
            }

            record.setResponse(new ResponseEntity<>(response, HttpStatus.OK));
            return record;
        } catch (ProjectNotFoundException exception) {
            record.setResponse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
            return record;
        }
    }

    private Project getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
    }

    public ParallelRunProjectTestRecord deleteProject(Long id) {
        ParallelRunProjectTestRecord<Void> record = new ParallelRunProjectTestRecord<>();
        try {
            Project project = getProjectById(id);
            record.setProject(project);
            projectRepository.delete(project);

            ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.OK);
            record.setResponse(response);
            return record;
        } catch (ProjectNotFoundException exception) {
            ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            record.setResponse(response);
            return record;
        }
    }

    public ParallelRunProjectTestRecord addTeam(Long projectId, Long teamId) {
        ParallelRunProjectTestRecord<Void> record = new ParallelRunProjectTestRecord<>();

        try {
            Project project = getProjectById(projectId);
            record.setProject(project);
            try {
                Team team = getTeamById(teamId);

                project.addTeam(team);
                team.setProject(project);
                record.setProject(project);
                record.setTeam(team);

                projectRepository.save(project);
                teamRepository.save(team);

                ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.OK);
                record.setResponse(response);
                return record;
            } catch (TeamNotFoundException exception) {
                ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                record.setResponse(response);
                return record;
            }
        } catch (ProjectNotFoundException exception) {
            ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            record.setResponse(response);
            return record;
        }
    }

    private Team getTeamById(Long id) {
        Optional<Team> team = teamRepository.findById(id);

        if (team.isEmpty()) {
            throw new TeamNotFoundException();
        }

        return team.get();
    }
}
