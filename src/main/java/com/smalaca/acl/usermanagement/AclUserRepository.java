package com.smalaca.acl.usermanagement;

import com.smalaca.parallelrun.usermanagement.ParallelRunUserInteractionRecord;
import com.smalaca.usermanagement.TeamRole;
import com.smalaca.usermanagement.User;
import com.smalaca.usermanagement.UserName;
import com.smalaca.usermanagement.UserRepository;

import java.util.Optional;

public class AclUserRepository implements UserRepository {
    private final com.smalaca.taskamanager.repository.UserRepository userRepository;
    private final ParallelRunUserInteractionRecord interaction;
    private boolean disabledOld;

    public AclUserRepository(
            com.smalaca.taskamanager.repository.UserRepository userRepository,
            ParallelRunUserInteractionRecord interaction) {
        this.userRepository = userRepository;
        this.interaction = interaction;
    }

    public void setDisabledOld(boolean disabledOld) {
        this.disabledOld = disabledOld;
    }

    @Override
    public User save(User user) {
        if (disabledOld) {
            com.smalaca.taskamanager.model.entities.User toSave = asLegacyUser(user);
            com.smalaca.taskamanager.model.entities.User saved = userRepository.save(toSave);
            return asUser(saved);
        } else {
            return user;
        }
    }

    @Override
    public Optional<User> findByUserNameFirstNameAndUserNameLastName(String firstName, String lastName) {
        if (disabledOld) {
            return asOptionalUser(userRepository.findByUserNameFirstNameAndUserNameLastName(firstName, lastName));
        } else {
            return interaction.existsUser(firstName, lastName);
        }
    }

    private Optional<com.smalaca.usermanagement.User> asOptionalUser(Optional<com.smalaca.taskamanager.model.entities.User> response) {
        return response.map(this::asUser);
    }

    private User asUser(com.smalaca.taskamanager.model.entities.User user) {
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setLogin(user.getLogin());
        newUser.setPassword(user.getPassword());
        newUser.setTeamRole(TeamRole.valueOf(user.getTeamRole().name()));
        UserName userName = new UserName();
        userName.setFirstName(user.getUserName().getFirstName());
        userName.setLastName(user.getUserName().getLastName());
        newUser.setUserName(userName);

        return newUser;
    }

    private com.smalaca.taskamanager.model.entities.User asLegacyUser(User user) {
        com.smalaca.taskamanager.model.entities.User legacyUser = new com.smalaca.taskamanager.model.entities.User();
        legacyUser.setId(user.getId());
        legacyUser.setLogin(user.getLogin());
        legacyUser.setPassword(user.getPassword());
        legacyUser.setTeamRole(com.smalaca.taskamanager.model.enums.TeamRole.valueOf(user.getTeamRole().name()));
        com.smalaca.taskamanager.model.embedded.UserName userName = new com.smalaca.taskamanager.model.embedded.UserName();
        userName.setFirstName(user.getUserName().getFirstName());
        userName.setLastName(user.getUserName().getLastName());
        legacyUser.setUserName(userName);

        return legacyUser;

    }
}
