package com.smalaca.projectmanagement;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ProductOwner {
    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }
}
