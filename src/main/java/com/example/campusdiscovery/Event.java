package com.example.campusdiscovery;

public class Event {
    private String name;
    private String description;
    private String location;
    private String time;
    private String host;

    public Event(String name, String description, String location, String time, String host) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.time = time;
        this.host = host;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() { return this.description; }

    public String getLocation() { return this.location; }

    public String getTime() { return this.time; }

    public String getHost() { return this.host; }
}
