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

    public void compareWith(ParallelRunUserTestRecord record) {
        if (isUserEqual(record) && isRequestEqual(record)) {
            System.out.println("RECORDS ARE THE SAME");
        } else {
            System.out.println("RECORDS ARE DIFFERENT");
        }
    }

    private boolean isUserEqual(ParallelRunUserTestRecord record) {
        return this.user.equals(record.user);
    }

    private boolean isRequestEqual(ParallelRunUserTestRecord record) {
        return record.response.getStatusCode().equals(this.response.getStatusCode());
    }
}
