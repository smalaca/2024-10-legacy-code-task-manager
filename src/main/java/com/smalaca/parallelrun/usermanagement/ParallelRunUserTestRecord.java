package com.smalaca.parallelrun.usermanagement;

import com.smalaca.taskamanager.model.entities.User;
import org.springframework.http.ResponseEntity;

public class ParallelRunUserTestRecord {
    private ResponseEntity<Void> response;
    private UserToCompare user;

    public ResponseEntity<Void> getResponse() {
        return response;
    }

    public void setResponse(ResponseEntity<Void> response) {
        this.response = response;
    }

    public void setUser(User user) {
        this.user = UserToCompare.createFrom(user);
    }

    public void setUser(com.smalaca.usermanagement.User user) {
        this.user = UserToCompare.createFrom(user);
    }

    public void compareWithoutUserId(ParallelRunUserTestRecord record) {
        if (isUserWithoutIdEqual(record) && isRequestEqual(record)) {
            System.out.println("RECORDS ARE THE SAME");
        } else {
            System.out.println("RECORDS ARE DIFFERENT");
        }
    }

    private boolean isUserWithoutIdEqual(ParallelRunUserTestRecord record) {
        if (user == null && record.user == null) {
            return true;
        }

        return this.user.isUserWithoutIdEqual(record.user);
    }

    private boolean isRequestEqual(ParallelRunUserTestRecord record) {
        return record.response.getStatusCode().equals(this.response.getStatusCode());
    }
}
