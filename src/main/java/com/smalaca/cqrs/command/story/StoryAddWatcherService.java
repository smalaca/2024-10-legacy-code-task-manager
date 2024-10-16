package com.smalaca.cqrs.command.story;

import com.smalaca.taskamanager.dto.WatcherDto;
import com.smalaca.taskamanager.model.entities.Story;
import com.smalaca.taskamanager.repository.StoryRepository;

import java.util.Optional;

public class StoryAddWatcherService {
    private final StoryRepository storyRepository;
    private final WatcherDomainModelRepository watcherDomainModelRepository;

    public StoryAddWatcherService(StoryRepository storyRepository, WatcherDomainModelRepository watcherDomainModelRepository) {
        this.storyRepository = storyRepository;
        this.watcherDomainModelRepository = watcherDomainModelRepository;
    }

    public StoryResponse addWatcherToStory(long id, WatcherDto dto) {
        StoryResponse storyResponse = new StoryResponse();
        Optional<Story> found = storyRepository.findById(id);
        storyResponse.storyDoesNotExist(found.isEmpty());

        if (!storyResponse.doesNotStoryExist()) {
            Story story = found.get();

            Optional<WatcherDomainModel> watcher = watcherDomainModelRepository.findById(dto.getId());
            storyResponse.userDoesNotExist(watcher.isEmpty());

            if (!storyResponse.isUserDoesNotExist()) {
                story.addWatcher(watcher.get().getWatcher());

                storyRepository.save(story);
            }
        }

        return storyResponse;
    }
}
