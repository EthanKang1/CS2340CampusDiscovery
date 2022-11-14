package com.example.campusdiscovery.models;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class EventUnitTest {
    // example event #1
    private final String EVENT1_NAME = "event1";
    private final String EVENT1_DESCRIPTION = "event1 description";
    private final String EVENT1_LOCATION = "event1 location";
    private final String EVENT1_TIME = "event1 time";
    private final String EVENT1_HOST = "event1 host";
    private final String EVENT1_CAPACITY = "5";
    private final String EVENT1_RSVPLIST = "Tony, Levi";

    // example event #2
    private final String EVENT2_NAME = "event2";
    private final String EVENT2_DESCRIPTION = "event2 description";
    private final String EVENT2_LOCATION = "event2 location";
    private final String EVENT2_TIME = "event2 time";
    private final String EVENT2_HOST = "event2 host";
    private final String EVENT2_CAPACITY = "2";
    private final String EVENT2_RSVPLIST = "Ethan, Levi";

    // example usernames
    private final String USERNAME = "testUsername";

    @Test
    public void createEvent_ValidInput() {
        Event testEvent1 = new Event(EVENT1_NAME,
                                    EVENT1_DESCRIPTION,
                                    EVENT1_LOCATION,
                                    EVENT1_TIME,
                                    EVENT1_CAPACITY,
                                    EVENT1_HOST,
                                    EVENT1_RSVPLIST);

        assertEquals(testEvent1.getName(), EVENT1_NAME);
        assertEquals(testEvent1.getDescription(), EVENT1_DESCRIPTION);
        assertEquals(testEvent1.getLocation(), EVENT1_LOCATION);
        assertEquals(testEvent1.getTime(), EVENT1_TIME);
        assertEquals(testEvent1.getHost(), EVENT1_HOST);
    }

    @Test
    public void multipleEvents_UniqueUUIDs() {
        Event testEvent1 = new Event(EVENT1_NAME,
                EVENT1_DESCRIPTION,
                EVENT1_LOCATION,
                EVENT1_TIME,
                EVENT1_CAPACITY,
                EVENT1_HOST,
                EVENT1_RSVPLIST);
        Event testEvent2 = new Event(EVENT2_NAME,
                EVENT2_DESCRIPTION,
                EVENT2_LOCATION,
                EVENT2_TIME,
                EVENT2_CAPACITY,
                EVENT2_HOST,
                EVENT2_RSVPLIST);

        assertNotEquals(testEvent1.getId(), testEvent2.getId());
    }

    @Test
    public void setStatus_ValidInput() {
        Event testEvent1 = new Event(EVENT1_NAME,
                EVENT1_DESCRIPTION,
                EVENT1_LOCATION,
                EVENT1_TIME,
                EVENT1_CAPACITY,
                EVENT1_HOST,
                EVENT1_RSVPLIST);

        testEvent1.setStatus(USERNAME, 1);

        assertEquals(testEvent1.getStatus(USERNAME), 1);
    }
}