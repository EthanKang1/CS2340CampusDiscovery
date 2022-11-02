package com.example.campusdiscovery;

import java.util.UUID;

public class Event {
    private UUID id;
    private String name;
    private String description;
    private String location;
    private String time;
    private String host;

    public Event(String name, String description, String location, String time, String host) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.location = location;
        this.time = time;
        this.host = host;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() { return this.description; }

    public String getLocation() { return this.location; }

    public String getTime() { return this.time; }

    public String getHost() { return this.host; }
}
