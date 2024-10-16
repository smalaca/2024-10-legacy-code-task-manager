package com.smalaca.taskamanager.api.rest;

import com.smalaca.acl.usermanagement.AclUserRepository;
import com.smalaca.parallelrun.usermanagement.ParallelRunUserTestRecord;
import com.smalaca.parallelrun.usermanagement.ParallelRunUserInteractionRecord;
import com.smalaca.taskamanager.dto.UserDto;
import com.smalaca.taskamanager.exception.UserNotFoundException;
import com.smalaca.taskamanager.model.embedded.EmailAddress;
import com.smalaca.taskamanager.model.embedded.PhoneNumber;
import com.smalaca.taskamanager.model.embedded.UserName;
import com.smalaca.taskamanager.model.entities.User;
import com.smalaca.taskamanager.model.enums.TeamRole;
import com.smalaca.taskamanager.repository.UserRepository;
import com.smalaca.usermanagement.UserDataTransferObject;
import com.smalaca.usermanagement.UserManagementApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@SuppressWarnings("checkstyle:ClassFanOutComplexity")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> usersDtos = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setFirstName(user.getUserName().getFirstName());
            userDto.setLastName(user.getUserName().getLastName());
            userDto.setLogin(user.getLogin());
            userDto.setPassword(user.getPassword());

            TeamRole teamRole = user.getTeamRole();
            if (teamRole != null) {
                userDto.setTeamRole(teamRole.name());
            }

            PhoneNumber phoneNumber = user.getPhoneNumber();
            if (phoneNumber != null) {
                userDto.setPhonePrefix(phoneNumber.getPrefix());
                userDto.setPhoneNumber(phoneNumber.getNumber());
            }

            EmailAddress emailAddress = user.getEmailAddress();
            if (emailAddress != null) {
                userDto.setEmailAddress(emailAddress.getEmailAddress());
            }

            usersDtos.add(userDto);
        }

        return new ResponseEntity<>(usersDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        try {
            User user = getUserById(id);

            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setFirstName(user.getUserName().getFirstName());
            userDto.setLastName(user.getUserName().getLastName());
            userDto.setLogin(user.getLogin());
            userDto.setPassword(user.getPassword());

            TeamRole teamRole = user.getTeamRole();
            if (teamRole != null) {
                userDto.setTeamRole(teamRole.name());
            }

            PhoneNumber phoneNumber = user.getPhoneNumber();
            if (phoneNumber != null) {
                userDto.setPhonePrefix(phoneNumber.getPrefix());
                userDto.setPhoneNumber(phoneNumber.getNumber());
            }

            EmailAddress emailAddress = user.getEmailAddress();
            if (emailAddress != null) {
                userDto.setEmailAddress(emailAddress.getEmailAddress());
            }

            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDto userDto, UriComponentsBuilder uriComponentsBuilder) {
        ParallelRunUserInteractionRecord interaction = new ParallelRunUserInteractionRecord();
        ParallelRunUserTestRecord record = createUserInLegacyCode(userDto, copyOf(uriComponentsBuilder), interaction);
        AclUserRepository aclUserRepository = new AclUserRepository(userRepository, interaction);
        ParallelRunUserTestRecord recordToCompare = new UserManagementApi(aclUserRepository)
                .createUser(asUserDataTransferObject(userDto), copyOf(uriComponentsBuilder));

        record.compareWithoutUserId(recordToCompare);

        return record.getResponse();
    }

    private UserDataTransferObject asUserDataTransferObject(UserDto userDto) {
        UserDataTransferObject dto = new UserDataTransferObject();
        dto.setId(userDto.getId());
        dto.setFirstName(userDto.getFirstName());
        dto.setLastName(userDto.getLastName());
        dto.setLogin(userDto.getLogin());
        dto.setPassword(userDto.getPassword());
        dto.setTeamRole(userDto.getTeamRole());

        return dto;
    }

    private UriComponentsBuilder copyOf(UriComponentsBuilder uriComponentsBuilder) {
        return uriComponentsBuilder == null ? null : uriComponentsBuilder.cloneBuilder();
    }

    private ParallelRunUserTestRecord createUserInLegacyCode(
            UserDto userDto, UriComponentsBuilder uriComponentsBuilder, ParallelRunUserInteractionRecord interaction) {
        ParallelRunUserTestRecord record = new ParallelRunUserTestRecord();
        if (exists(userDto, interaction)) {
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

    private boolean exists(UserDto userDto, ParallelRunUserInteractionRecord interaction) {
        Optional<User> response = userRepository.findByUserNameFirstNameAndUserNameLastName(userDto.getFirstName(), userDto.getLastName());
        interaction.registerExistsUser(userDto.getFirstName(), userDto.getLastName(), response);

        return !response.isEmpty();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
        User user;

        try {
            user = getUserById(id);
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (userDto.getLogin() != null) {
            user.setLogin(userDto.getLogin());
        }

        if (userDto.getPassword() != null) {
            user.setPassword(userDto.getPassword());
        }

        if (userDto.getPhoneNumber() != null) {
            PhoneNumber phoneNumber = new PhoneNumber();
            phoneNumber.setPrefix(userDto.getPhonePrefix());
            phoneNumber.setNumber(userDto.getPhoneNumber());
            user.setPhoneNumber(phoneNumber);
        }

        if (userDto.getEmailAddress() != null) {
            EmailAddress emailAddress = new EmailAddress();
            emailAddress.setEmailAddress(userDto.getEmailAddress());
            user.setEmailAddress(emailAddress);
        }

        if (userDto.getTeamRole() != null) {
            user.setTeamRole(TeamRole.valueOf(userDto.getTeamRole()));
        }
        
        User updated = userRepository.save(user);

        UserDto response = new UserDto();
        response.setId(updated.getId());
        response.setFirstName(updated.getUserName().getFirstName());
        response.setLastName(updated.getUserName().getLastName());
        response.setLogin(updated.getLogin());
        response.setPassword(updated.getPassword());

        TeamRole teamRole = updated.getTeamRole();
        if (teamRole != null) {
            response.setTeamRole(teamRole.name());
        }

        PhoneNumber phoneNumber = updated.getPhoneNumber();
        if (phoneNumber != null) {
            response.setPhonePrefix(phoneNumber.getPrefix());
            response.setPhoneNumber(phoneNumber.getNumber());
        }

        EmailAddress emailAddress = updated.getEmailAddress();
        if (emailAddress != null) {
            response.setEmailAddress(emailAddress.getEmailAddress());
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        User user;

        try {
            user = getUserById(id);
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userRepository.delete(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private User getUserById(Long id) {
        Optional<User> user;
        user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        return user.get();
    }
}
