package com.smalaca.cqrs.command.team;

import java.util.Optional;

public interface TeamDomainModelRepository {
    void save(TeamDomainModel teamDomainModel);

    Optional<TeamDomainModel> findById(Long id);
}
