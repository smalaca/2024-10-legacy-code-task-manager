package com.smalaca.cqrs.acl.story;

import com.smalaca.cqrs.command.story.WatcherDomainModel;
import com.smalaca.cqrs.command.story.WatcherDomainModelRepository;
import com.smalaca.taskamanager.model.embedded.EmailAddress;
import com.smalaca.taskamanager.model.embedded.PhoneNumber;
import com.smalaca.taskamanager.model.embedded.Watcher;
import com.smalaca.taskamanager.model.entities.User;
import com.smalaca.taskamanager.repository.UserRepository;

import java.util.Optional;

public class AclWatcherDomainModelRepository implements WatcherDomainModelRepository {
    private final UserRepository userRepository;

    public AclWatcherDomainModelRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<WatcherDomainModel> findById(Long id) {
        Optional<User> found = userRepository.findById(id);

        if (found.isPresent()) {
            User user = found.get();
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

            return Optional.of(new WatcherDomainModel(watcher));
        }

        return Optional.empty();
    }
}
