package com.smalaca.parallelrun.usermanagement;

import com.smalaca.taskamanager.model.entities.User;
import com.smalaca.usermanagement.TeamRole;
import com.smalaca.usermanagement.UserName;

import java.util.Optional;

public class ParallelRunUserInteractionRecord {
    private String firstName;
    private String lastName;
    private Optional<User> response;

    public void registerExistsUser(String firstName, String lastName, Optional<User> response) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.response = response;
    }

    public Optional<com.smalaca.usermanagement.User> existsUser(String firstName, String lastName) {
        if (this.firstName.equals(firstName) && this.lastName.equals(lastName)) {
            return asOptionalUser(response);
        } else {
            throw new RuntimeException("SOMETHING WENT WRONG");
        }
    }

    private Optional<com.smalaca.usermanagement.User> asOptionalUser(Optional<User> response) {
        Optional<com.smalaca.usermanagement.User> found = response.map(user -> {
            com.smalaca.usermanagement.User newUser = new com.smalaca.usermanagement.User();
            newUser.setId(user.getId());
            newUser.setLogin(user.getLogin());
            newUser.setPassword(user.getPassword());
            newUser.setTeamRole(TeamRole.valueOf(user.getTeamRole().name()));
            UserName userName = new UserName();
            userName.setFirstName(user.getUserName().getFirstName());
            userName.setLastName(user.getUserName().getLastName());
            newUser.setUserName(userName);

            return newUser;
        });
        return found;
    }
}
