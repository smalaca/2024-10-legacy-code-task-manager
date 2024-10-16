package com.smalaca.acl.usermanagement;

import com.smalaca.parallelrun.usermanagement.ParallelRunUserInteractionRecord;
import com.smalaca.usermanagement.User;
import com.smalaca.usermanagement.UserRepository;

import java.util.Optional;

public class AclUserRepository implements UserRepository {
    private final com.smalaca.taskamanager.repository.UserRepository userRepository;
    private final ParallelRunUserInteractionRecord interaction;

    public AclUserRepository(
            com.smalaca.taskamanager.repository.UserRepository userRepository,
            ParallelRunUserInteractionRecord interaction) {
        this.userRepository = userRepository;
        this.interaction = interaction;
    }

    @Override
    public User save(User user) {
//        return userRepository.save(user);
        return user;
    }

    @Override
    public Optional<User> findByUserNameFirstNameAndUserNameLastName(String firstName, String lastName) {
        return interaction.existsUser(firstName, lastName);
    }
}
