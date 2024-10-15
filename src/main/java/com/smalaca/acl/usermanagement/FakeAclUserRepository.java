package com.smalaca.acl.usermanagement;

import com.smalaca.usermanagement.User;
import com.smalaca.usermanagement.UserRepository;

import java.util.Optional;

public class FakeAclUserRepository implements UserRepository {
    @Override
    public User save(User user) {
        return user;
    }

    @Override
    public Optional<User> findByUserNameFirstNameAndUserNameLastName(String firstName, String lastName) {
        return Optional.empty();
    }
}
