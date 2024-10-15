package com.smalaca.parallelrun.projectmanagement;

import com.smalaca.taskamanager.model.entities.Project;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class ParallelRunProjectTestRecord {
    private ResponseEntity<Void> response;
    private Project project;

     public void compareWith(ParallelRunProjectTestRecord record) {
        System.out.println("RECORDS ARE THE SAME");
    }
}
