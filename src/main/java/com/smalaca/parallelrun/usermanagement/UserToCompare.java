package com.smalaca.parallelrun.usermanagement;

import com.smalaca.taskamanager.model.entities.User;

class UserToCompare {
    private Long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String teamRole;

    static UserToCompare createFrom(User user) {
        UserToCompare compare = new UserToCompare();
        compare.id = user.getId();
        compare.login = user.getLogin();
        compare.password = user.getPassword();
        compare.firstName = user.getUserName().getFirstName();
        compare.lastName = user.getUserName().getLastName();
        compare.teamRole = user.getTeamRole().name();

        return compare;
    }

    static UserToCompare createFrom(com.smalaca.usermanagement.User user) {
        UserToCompare compare = new UserToCompare();
        compare.id = user.getId();
        compare.login = user.getLogin();
        compare.password = user.getPassword();
        compare.firstName = user.getUserName().getFirstName();
        compare.lastName = user.getUserName().getLastName();
        compare.teamRole = user.getTeamRole().name();

        return compare;
    }

    boolean isUserWithoutIdEqual(UserToCompare compare) {
        return areEqual(login, compare.login)
                && areEqual(password, compare.password)
                && areEqual(firstName, compare.firstName)
                && areEqual(lastName, compare.lastName)
                && areEqual(teamRole, compare.teamRole);
    }

    private boolean areEqual(String value, String toCompare) {
        return (value == null && toCompare == null) || value.equals(toCompare);
    }
}
