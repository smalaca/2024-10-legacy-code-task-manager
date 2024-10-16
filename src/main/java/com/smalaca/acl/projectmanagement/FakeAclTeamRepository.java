package com.smalaca.acl.projectmanagement;

import com.smalaca.projectmanagement.Team;
import com.smalaca.projectmanagement.TeamRepository;

import java.util.Optional;

public class FakeAclTeamRepository implements TeamRepository {
    @Override
    public Optional<Team> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void save(Team team) {

    }
}
