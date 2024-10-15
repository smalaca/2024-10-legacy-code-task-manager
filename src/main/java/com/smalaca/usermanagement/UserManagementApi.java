package com.smalaca.usermanagement;

import com.smalaca.parallelrun.usermanagement.ParallelRunUserTestRecord;
import com.smalaca.taskamanager.dto.UserDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

public class UserManagementApi {
    private final UserRepository userRepository;

    public UserManagementApi(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ParallelRunUserTestRecord createUser(UserDto userDto, UriComponentsBuilder uriComponentsBuilder) {
        ParallelRunUserTestRecord record = new ParallelRunUserTestRecord();
        if (exists(userDto)) {
            ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.CONFLICT);
            record.setResponse(response);
            return record;
        } else {
            User user = new User();
            user.setTeamRole(TeamRole.valueOf(userDto.getTeamRole()));
            UserName userName = new UserName();
            userName.setFirstName(userDto.getFirstName());
            userName.setLastName(userDto.getLastName());
            user.setUserName(userName);
            user.setLogin(userDto.getLogin());
            user.setPassword(userDto.getPassword());

            record.setUser(user);
            User saved = userRepository.save(user);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriComponentsBuilder.path("/user/{id}").buildAndExpand(saved.getId()).toUri());
            ResponseEntity<Void> response = new ResponseEntity<>(headers, HttpStatus.CREATED);
            record.setResponse(response);
            return record;
        }
    }

    private boolean exists(UserDto userDto) {
        return !userRepository.findByUserNameFirstNameAndUserNameLastName(userDto.getFirstName(), userDto.getLastName()).isEmpty();
    }
}
