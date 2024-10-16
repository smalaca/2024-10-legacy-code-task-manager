package com.smalaca.cqrs.command.story;

import com.smalaca.taskamanager.model.embedded.Watcher;

public class WatcherDomainModel {
    private final Watcher watcher;

    public WatcherDomainModel(Watcher watcher) {
        this.watcher = watcher;
    }

    public Watcher getWatcher() {
        return watcher;
    }
}
