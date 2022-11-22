package com.example.campusdiscovery.models;

import java.util.UUID;

public class Attendee {
    private UUID id;
    private String name;

    public Attendee(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }
}
