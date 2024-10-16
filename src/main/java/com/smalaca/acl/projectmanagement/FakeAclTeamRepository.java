package com.smalaca.acl.projectmanagement;

import com.smalaca.parallelrun.projectmanagement.ParallelRunProjectInteraction;
import com.smalaca.projectmanagement.Team;
import com.smalaca.projectmanagement.TeamRepository;

import java.util.Optional;

public class FakeAclTeamRepository implements TeamRepository {
    private final ParallelRunProjectInteraction interaction;

    public FakeAclTeamRepository(ParallelRunProjectInteraction interaction) {
        this.interaction = interaction;
    }

    @Override
    public Optional<Team> findById(Long id) {
        return interaction.findTeamById(id);
    }

    @Override
    public void save(Team team) {

    }
}
