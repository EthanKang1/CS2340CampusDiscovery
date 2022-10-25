package com.example.campusdiscovery;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

public class Events {
    List<Event> eventList = new ArrayList<>();

    public Events() {

    }

    public void addEvent(Event event) {
        eventList.add(event);
    }

    public String toJson() {
        Gson gson = new Gson();
        String menuJson = gson.toJson(this.eventList);
        return menuJson;
    }
}
