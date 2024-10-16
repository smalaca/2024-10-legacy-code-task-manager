package com.smalaca.taskamanager.api.rest;

import com.smalaca.acl.projectmanagement.FakeAclProjectRepository;
import com.smalaca.parallelrun.projectmanagement.ParallelRunProjectTestRecord;
import com.smalaca.projectmanagement.ProjectManagementApi;
import com.smalaca.taskamanager.dto.ProjectDto;
import com.smalaca.taskamanager.exception.ProjectNotFoundException;
import com.smalaca.taskamanager.exception.TeamNotFoundException;
import com.smalaca.taskamanager.model.entities.Project;
import com.smalaca.taskamanager.model.entities.Team;
import com.smalaca.taskamanager.model.enums.ProjectStatus;
import com.smalaca.taskamanager.repository.ProjectRepository;
import com.smalaca.taskamanager.repository.TeamRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;
    private final ProjectManagementApi projectManagementApi;

    public ProjectController(ProjectRepository projectRepository, TeamRepository teamRepository) {
        this.projectRepository = projectRepository;
        this.teamRepository = teamRepository;
        projectManagementApi = new ProjectManagementApi(new FakeAclProjectRepository());
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        List<ProjectDto> projectsDtos = new ArrayList<>();

        for (Project project : projectRepository.findAll()) {
            ProjectDto projectDto = new ProjectDto();
            projectDto.setId(project.getId());
            projectDto.setName(project.getName());
            projectDto.setProjectStatus(project.getProjectStatus().name());

            if (project.getProductOwner() != null) {
                projectDto.setProductOwnerId(project.getProductOwner().getId());
            }

            projectsDtos.add(projectDto);
        }

        return new ResponseEntity<>(projectsDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @Transactional
    public ResponseEntity<ProjectDto> getProject(@PathVariable("id") Long id) {
        try {
            Project project = getProjectById(id);

            ProjectDto projectDto = new ProjectDto();
            projectDto.setId(project.getId());
            projectDto.setName(project.getName());
            projectDto.setProjectStatus(project.getProjectStatus().name());

            if (project.getProductOwner() != null) {
                projectDto.setProductOwnerId(project.getProductOwner().getId());
            }

            List<Long> ids = project
                    .getTeams()
                    .stream()
                    .map(Team::getId)
                    .collect(Collectors.toList());

            projectDto.setTeamIds(ids);

            return new ResponseEntity<>(projectDto, HttpStatus.OK);
        } catch (ProjectNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Void> createProject(@RequestBody ProjectDto projectDto, UriComponentsBuilder uriComponentsBuilder) {
        ParallelRunProjectTestRecord record = createProjectLegacy(projectDto, uriComponentsBuilder.cloneBuilder());
        ParallelRunProjectTestRecord recordToCompare = projectManagementApi.createProject(projectDto, uriComponentsBuilder.cloneBuilder());

        record.compareWithoutId(recordToCompare);

        return record.getResponse();
    }

    private ParallelRunProjectTestRecord createProjectLegacy(ProjectDto projectDto, UriComponentsBuilder uriComponentsBuilder) {
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

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable("id") Long id, @RequestBody ProjectDto projectDto) {
        ParallelRunProjectTestRecord record = updateProjectLegacy(id, projectDto);
        ParallelRunProjectTestRecord recordToCompare = projectManagementApi.updateProject(id, projectDto);

        record.compareWithoutId(recordToCompare);
        return record.getResponse();
    }

    private ParallelRunProjectTestRecord updateProjectLegacy(Long id, ProjectDto projectDto) {
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

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable("id") Long id) {
        ParallelRunProjectTestRecord record = deleteProjectLegacy(id);
        ParallelRunProjectTestRecord recordToCompare = projectManagementApi.deleteProject(id);

        record.compareWithoutId(recordToCompare);

        return record.getResponse();
    }

    private ParallelRunProjectTestRecord deleteProjectLegacy(Long id) {
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


    @PutMapping("/{projectId}/teams/{teamId}")
    @Transactional
    public ResponseEntity<Void> addTeam(@PathVariable Long projectId, @PathVariable Long teamId) {
        ParallelRunProjectTestRecord<Void> record = addTeamLegacy(projectId, teamId);
        return record.getResponse();
    }

    private ParallelRunProjectTestRecord<Void> addTeamLegacy(Long projectId, Long teamId) {
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

    @DeleteMapping("/{projectId}/teams/{teamId}")
    @Transactional
    public ResponseEntity<Void> removeTeam(@PathVariable Long projectId, @PathVariable Long teamId) {
        ParallelRunProjectTestRecord record = removeTeamLegacy(projectId, teamId);
        return record.getResponse();
    }

    private ParallelRunProjectTestRecord removeTeamLegacy(Long projectId, Long teamId) {
        ParallelRunProjectTestRecord<Void> record = new ParallelRunProjectTestRecord<>();
        try {
            Project project = getProjectById(projectId);
            record.setProject(project);

            try {
                Team team = getTeamById(teamId);

                project.removeTeam(team);
                team.setProject(null);

                projectRepository.save(project);
                teamRepository.save(team);
                record.setProject(project);
                record.setTeam(team);

                record.setResponse(new ResponseEntity<>(HttpStatus.OK));
            } catch (TeamNotFoundException exception) {
                record.setResponse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
            }

        } catch (ProjectNotFoundException exception) {
            record.setResponse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        return record;
    }

    private Project getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
    }

    private Team getTeamById(Long id) {
        Optional<Team> team = teamRepository.findById(id);

        if (team.isEmpty()) {
            throw new TeamNotFoundException();
        }

        return team.get();
    }
}
