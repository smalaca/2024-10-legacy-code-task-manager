package com.smalaca.usermanagement;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@SuppressWarnings("MethodCount")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String login;
    private String password;

    @Embedded
    private UserName userName;

    @Enumerated(EnumType.STRING)
    private TeamRole teamRole;

    public UserName getUserName() {
        return userName;
    }

    void setUserName(UserName userName) {
        this.userName = userName;
    }

    public String getLogin() {
        return login;
    }

    void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    public TeamRole getTeamRole() {
        return teamRole;
    }

    void setTeamRole(TeamRole teamRole) {
        this.teamRole = teamRole;
    }

    public Long getId() {
        return id;
    }
}
