package com.github.disc99.orm;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PersonInfo {
    @Id
    private Long id;
    private String fullName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }

    @Override
    public String toString() {
        return "PersonInfo [id=" + id + ", fullName=" + fullName + "]";
    }

}