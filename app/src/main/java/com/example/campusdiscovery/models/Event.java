package com.example.campusdiscovery.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Event {
    private UUID id;
    private String name;
    private String description;
    private String location;
    private String time;
    private String RSVPList;
    private String capacity;
    private Map<String, Integer> statusMap = new HashMap<String, Integer>();
    private final int DEFAULT_STATUS = 2;
    private final int BAD_DEFAULT_STATUS = 0;

    private Attendee host;

    // development
    private Map<UUID, Integer> attendeeMap = new HashMap<UUID, Integer>();

    public Event(String name, String description, String location, String time, String capacity, String RSVPList) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.location = location;
        this.time = time;
        this.capacity = capacity;
        this.RSVPList = RSVPList;
        this.host = null;
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

//    public int getStatus(String username) {
//        if (username == null) {
//            return -1;
//        }
//
//        if (this.RSVPList == null) {
//            this.RSVPList = "";
//        }
//        List<String> RSVPList1 = Arrays.asList(this.RSVPList.split(","));
//        if ((RSVPList1.contains(username) || RSVPList1.contains("") || username.equals(this.host)) && this.statusMap.get(username) == null) {
//            this.setStatus(username, DEFAULT_STATUS);
//            return this.statusMap.get(username);
//        }
//        if (this.statusMap.get(username) == null) {
//            this.setStatus(username, BAD_DEFAULT_STATUS);
//        }
//        return this.statusMap.get(username);
//    }

    public List<String> getRSVPList() {
        if (this.RSVPList == null) {
            this.RSVPList = "";
        }
        List<String> RSVPList1 = Arrays.asList(this.RSVPList.split(","));
        return RSVPList1;
    }

    public void setStatus(String username, int status) {
        if (username == null) {
            return;
        }
        this.statusMap.put(username, status);
    }
    public String getCapacity() {
        return capacity;
    }

    public Map<String, Integer> getStatusMap() {
        return statusMap;
    }

    public String getAttendees() {
        int attendees = 0;
        if (statusMap.size() > 0) {
            for (String entry : statusMap.keySet()) {
                if (statusMap.get(entry) == 0) {
                    attendees++;
                }
            }
        }
        return Integer.toString(attendees);
    }

    public Map<UUID, Integer> getAttendeeMap() {
        return attendeeMap;
    }

    public Integer getAttendeeStatus(Attendee attendee) {
        return this.attendeeMap.get(attendee.getId());
    }

    public void setAttendee(UUID attendeeId, Integer status) {
        this.attendeeMap.put(attendeeId, status);
        System.out.println(this.attendeeMap);
    }

    public void setHost(Attendee attendee) {
        this.host = attendee;
    }

    public Attendee getHost() {
        return host;
    }
}
