package com.smalaca.cqrs.command.story;

import com.smalaca.taskamanager.dto.WatcherDto;
import com.smalaca.taskamanager.model.embedded.EmailAddress;
import com.smalaca.taskamanager.model.embedded.PhoneNumber;
import com.smalaca.taskamanager.model.embedded.Watcher;
import com.smalaca.taskamanager.model.entities.Story;
import com.smalaca.taskamanager.model.entities.User;
import com.smalaca.taskamanager.repository.StoryRepository;
import com.smalaca.taskamanager.repository.UserRepository;

import java.util.Optional;

public class StoryAddWatcherService {
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;

    public StoryAddWatcherService(StoryRepository storyRepository, UserRepository userRepository) {
        this.storyRepository = storyRepository;
        this.userRepository = userRepository;
    }

    public StoryResponse addWatcherToStory(long id, WatcherDto dto) {
        StoryResponse storyResponse = new StoryResponse();
        Optional<Story> found = storyRepository.findById(id);
        storyResponse.storyDoesNotExist(found.isEmpty());

        if (!storyResponse.doesNotStoryExist()) {
            Story story = found.get();

            Optional<User> found1 = userRepository.findById(dto.getId());
            storyResponse.userDoesNotExist(found1.isEmpty());
            if (!storyResponse.isUserDoesNotExist()) {
                User user = found1.get();
                Watcher watcher = new Watcher();
                watcher.setLastName(user.getUserName().getLastName());
                watcher.setFirstName(user.getUserName().getFirstName());

                EmailAddress userEmailAddress = user.getEmailAddress();
                PhoneNumber userPhoneNumber = user.getPhoneNumber();

                if (userPhoneNumber != null) {
                    PhoneNumber phoneNumber = new PhoneNumber();
                    phoneNumber.setNumber(userPhoneNumber.getNumber());
                    phoneNumber.setPrefix(userPhoneNumber.getPrefix());
                    watcher.setPhoneNumber(phoneNumber);
                }

                if (userEmailAddress != null) {
                    EmailAddress emailAddress = new EmailAddress();
                    emailAddress.setEmailAddress(userEmailAddress.getEmailAddress());
                    watcher.setEmailAddress(emailAddress);
                }
                story.addWatcher(watcher);

                storyRepository.save(story);
            }
        }
        return storyResponse;
    }
}
