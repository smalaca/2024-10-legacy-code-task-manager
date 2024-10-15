package com.smalaca.parallelrun.usermanagement;

import com.smalaca.taskamanager.model.entities.User;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
class UserToCompare {
    private Long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String phonePrefix;
    private String phoneNumber;
    private String emailAddress;
    private String teamRole;

    static UserToCompare createFrom(User user) {
        UserToCompare compare = new UserToCompare();
        compare.id = user.getId();
        compare.login = user.getLogin();
        compare.password = user.getPassword();
        compare.firstName = user.getUserName().getFirstName();
        compare.lastName = user.getUserName().getLastName();

        if (user.getPhoneNumber() != null) {
            compare.phonePrefix = user.getPhoneNumber().getPrefix();
            compare.phoneNumber = user.getPhoneNumber().getNumber();
        }

        if (user.getEmailAddress() != null) {
            compare.emailAddress = user.getEmailAddress().getEmailAddress();
        }

        compare.teamRole = user.getTeamRole().name();

        return compare;
    }
}
