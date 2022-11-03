package com.example.campusdiscovery;

import java.util.ArrayList;
import java.util.List;

class Pagination {
    private final int eventsPerPage;
    private final int lastPageItems;
    private final int lastPage;
    private final List<Event> eventData;

    public Pagination(int eventsPerPage, List<Event> eventData) {
        this.eventsPerPage = eventsPerPage;
        this.eventData = eventData;
        int eventTotal = eventData.size();
        this.lastPage = (eventTotal / eventsPerPage);
        this.lastPageItems = (eventTotal % eventsPerPage);
    }

    public List<Event> getEventData(int currentPage) {
        int startItem = currentPage * eventsPerPage;
        List<Event> newPageData = new ArrayList<>();
        if (currentPage == lastPage) {
            for (int c = 0; c < lastPageItems; c++) {
                newPageData.add(eventData.get(startItem + c));
            }
        } else {
            for (int c = 0; c < eventsPerPage; c++) {
                newPageData.add(eventData.get(startItem + c));
            }
        }
        return newPageData;
    }

    public int getLastPage() {
        return lastPage;
    }
}
