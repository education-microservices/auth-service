package com.example.azure.persistence.models;

import org.springframework.data.annotation.Id;

public class ItemModel {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
