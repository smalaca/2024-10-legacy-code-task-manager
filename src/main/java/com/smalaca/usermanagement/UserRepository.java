package com.smalaca.usermanagement;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findByUserNameFirstNameAndUserNameLastName(String firstName, String lastName);
}
