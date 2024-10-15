package com.smalaca.usermanagement;

import javax.persistence.Embeddable;

@Embeddable
public class UserName {
    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
