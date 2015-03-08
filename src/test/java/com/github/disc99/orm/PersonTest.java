package com.github.disc99.orm;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PersonTest {
    @Id
    private Long id;
    private String simpleName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String name) {
        this.simpleName = name;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + simpleName + "]";
    }

}