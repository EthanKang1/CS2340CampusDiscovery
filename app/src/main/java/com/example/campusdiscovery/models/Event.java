package com.example.campusdiscovery.models;

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
    private String host;
    private String RSVPList;
    private Map<String, Integer> statusMap = new HashMap<String, Integer>();

    private final int DEFAULT_STATUS = 2;
    private final int BAD_DEFAULT_STATUS = 0;

    public Event(String name, String description, String location, String time, String host, String RSVPList) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.location = location;
        this.time = time;
        this.host = host;
        this.RSVPList = RSVPList;
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
        if (this.RSVPList == null) {
            this.RSVPList = "";
        }
        List<String> RSVPList1 = Arrays.asList(this.RSVPList.split(","));
        if ((RSVPList1.contains(username) || RSVPList1.contains("") || username.equals(this.host)) && this.statusMap.get(username) == null) {
            this.setStatus(username, DEFAULT_STATUS);
            return this.statusMap.get(username);
        }
        if (this.statusMap.get(username) == null) {
            this.setStatus(username, BAD_DEFAULT_STATUS);
        }
        return this.statusMap.get(username);
    }

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
}
