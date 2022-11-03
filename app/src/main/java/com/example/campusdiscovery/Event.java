package com.example.campusdiscovery;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Event {
    private UUID id;
    private String name;
    private String description;
    private String location;
    private String time;
    private String host;
    private Map<String, Integer> statusMap = new HashMap<String, Integer>();

    private final int DEFAULT_STATUS = 2;

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

    public String getDescription() {
        return this.description;
    }

    public String getLocation() {
        return this.location;
    }

    public String getTime() {
        return this.time;
    }

    public String getHost() {
        return this.host;
    }

    public int getStatus(String username) {
        if (username == null) {
            return -1;
        }

        if (this.statusMap.get(username) == null) {
            this.setStatus(username, DEFAULT_STATUS);
        }
        return this.statusMap.get(username);
    }

    public void setStatus(String username, int status) {
        if (username == null) {
            return;
        }
        this.statusMap.put(username, status);
    }
}
