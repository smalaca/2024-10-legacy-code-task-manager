package com.smalaca.cqrs.command.story;

import java.util.Optional;

public interface WatcherDomainModelRepository {
    Optional<WatcherDomainModel> findById(Long id);
}
