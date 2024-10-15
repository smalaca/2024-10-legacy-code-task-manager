package com.smalaca.parallelrun;

import com.smalaca.taskamanager.model.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class ParallelRunUserTestRecord {
    private ResponseEntity<Void> response;
    private User user;

     public void compareWith(ParallelRunUserTestRecord record) {
        System.out.println("RECORDS ARE THE SAME");

    }
}
