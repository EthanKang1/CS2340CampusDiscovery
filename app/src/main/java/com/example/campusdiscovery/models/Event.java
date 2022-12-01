package com.example.campusdiscovery.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Event implements Comparable<Event>{
    private UUID id;
    private String name;
    private String description;
    private String location;
    private String time;
    private String RSVPList;
    private int attendeeCount;
    private int capacity;
    private Attendee host;

    // development
    private Map<UUID, Status> attendeeMap;

    public Event(String name,
                 String description,
                 String location,
                 String time,
                 int capacity,
                 String RSVPList) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.location = location;
        this.time = time;
        this.attendeeCount = 0;
        this.capacity = capacity;
        this.RSVPList = RSVPList;
        this.host = null;
        this.attendeeMap = new HashMap<UUID, Status>();
    }


    // Getter methods
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

    public int getCapacity() {
        return capacity;
    }

    // Setter methods
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


    // breaker
    public List<String> getRSVPList() {
        if (this.RSVPList == null) {
            this.RSVPList = "";
        }
        List<String> RSVPList1 = Arrays.asList(this.RSVPList.split(","));
        return RSVPList1;
    }

    public Map<UUID, Status> getAttendeeMap() {
        return attendeeMap;
    }

    public Status getAttendeeStatus(Attendee attendee) {
        return this.attendeeMap.get(attendee.getId());
    }

    public void setAttendee(UUID attendeeId, Status status) {
        this.attendeeMap.put(attendeeId, status);
        System.out.println(this.attendeeMap);
        this.refreshAttendeeCount();
    }

    public void setHost(Attendee attendee) {
        this.host = attendee;
    }

    public Attendee getHost() {
        return host;
    }


    public int getAttendeeCount() {
        return attendeeCount;
    }

    private void refreshAttendeeCount() {
        this.attendeeCount = 0;
        for(Status status : this.getAttendeeMap().values()) {
            if (status.equals(Status.ATTEND)) {
                this.attendeeCount++;
            }
        }
    }
}
