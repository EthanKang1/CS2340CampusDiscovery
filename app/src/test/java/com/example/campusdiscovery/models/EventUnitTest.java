package com.example.campusdiscovery.models;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private final int EVENT1_CAPACITY = 5;
    private final String EVENT1_RSVPLIST = "Tony,Levi";

    // example event #2
    private final String EVENT2_NAME = "event2";
    private final String EVENT2_DESCRIPTION = "event2 description";
    private final String EVENT2_LOCATION = "event2 location";
    private final String EVENT2_TIME = "event2 time";
    private final String EVENT2_HOST = "event2 host";
    private final int EVENT2_CAPACITY = 1;
    private final String EVENT2_RSVPLIST = "Ethan,Levi";
//
//    // example usernames
//    private final String USERNAME = "testUsername";

    @Test
    public void createEvent_ValidInput() {
        Event testEvent1 = new Event(EVENT1_NAME,
                                    EVENT1_DESCRIPTION,
                                    EVENT1_LOCATION,
                                    EVENT1_TIME,
                                    EVENT1_CAPACITY,
                                    EVENT1_RSVPLIST);

        assertEquals(testEvent1.getName(), EVENT1_NAME);
        assertEquals(testEvent1.getDescription(), EVENT1_DESCRIPTION);
        assertEquals(testEvent1.getLocation(), EVENT1_LOCATION);
        assertEquals(testEvent1.getTime(), EVENT1_TIME);
        assertEquals(testEvent1.getCapacity(), EVENT1_CAPACITY);
        assertEquals(testEvent1.getHost(), null);
    }

    @Test
    public void createEvent_ModifyEvent() {
        Event testEvent1 = new Event(EVENT1_NAME,
                EVENT1_DESCRIPTION,
                EVENT1_LOCATION,
                EVENT1_TIME,
                EVENT1_CAPACITY,
                EVENT1_RSVPLIST);

        assertEquals(testEvent1.getName(), EVENT1_NAME);
        assertEquals(testEvent1.getDescription(), EVENT1_DESCRIPTION);
        assertEquals(testEvent1.getLocation(), EVENT1_LOCATION);
        assertEquals(testEvent1.getTime(), EVENT1_TIME);
        assertEquals(testEvent1.getCapacity(), EVENT1_CAPACITY);
        assertEquals(testEvent1.getHost(), null);


        testEvent1.setName(EVENT2_NAME);
        testEvent1.setDescription(EVENT2_DESCRIPTION);
        testEvent1.setLocation(EVENT2_LOCATION);
        testEvent1.setTime(EVENT2_TIME);
        testEvent1.setCapacity(EVENT2_CAPACITY);


        assertEquals(testEvent1.getName(), EVENT2_NAME);
        assertEquals(testEvent1.getDescription(), EVENT2_DESCRIPTION);
        assertEquals(testEvent1.getLocation(), EVENT2_LOCATION);
        assertEquals(testEvent1.getTime(), EVENT2_TIME);
        assertEquals(testEvent1.getCapacity(), EVENT2_CAPACITY);
        assertEquals(testEvent1.getHost(), null);
    }


    @Test
    public void multipleEvents_UniqueUUIDs() {
        Event testEvent1 = new Event(EVENT1_NAME,
                EVENT1_DESCRIPTION,
                EVENT1_LOCATION,
                EVENT1_TIME,
                EVENT1_CAPACITY,
                EVENT1_RSVPLIST);
        Event testEvent2 = new Event(EVENT2_NAME,
                EVENT2_DESCRIPTION,
                EVENT2_LOCATION,
                EVENT2_TIME,
                EVENT2_CAPACITY,
                EVENT2_RSVPLIST);

        assertNotEquals(testEvent1.getId(), testEvent2.getId());
    }

    @Test
    public void addAttendee_ValidStatus() {
        Event testEvent1 = new Event(EVENT1_NAME,
                EVENT1_DESCRIPTION,
                EVENT1_LOCATION,
                EVENT1_TIME,
                EVENT1_CAPACITY,
                EVENT1_RSVPLIST);

        assertEquals(testEvent1.getAttendeeMap().size(), 0);
        assertEquals(testEvent1.getAttendeeCount(), 0);

        Attendee attendee1 = new Attendee("attendee1");
        Attendee attendee2 = new Attendee("attendee2");
        testEvent1.setAttendee(attendee1.getId(), Status.ATTEND);
        testEvent1.setAttendee(attendee2.getId(), Status.NO_ATTEND);

        assertEquals(testEvent1.getAttendeeMap().get(attendee1.getId()), Status.ATTEND);
        assertEquals(testEvent1.getAttendeeStatus(attendee1), Status.ATTEND);
        assertEquals(testEvent1.getAttendeeMap().get(attendee2.getId()), Status.NO_ATTEND);
        assertEquals(testEvent1.getAttendeeStatus(attendee2), Status.NO_ATTEND);
        assertEquals(testEvent1.getAttendeeCount(), 1);

    }

    @Test
    public void createEvent_UpdateHost() {
        Event testEvent1 = new Event(EVENT1_NAME,
                EVENT1_DESCRIPTION,
                EVENT1_LOCATION,
                EVENT1_TIME,
                EVENT1_CAPACITY,
                EVENT1_RSVPLIST);

        assertEquals(testEvent1.getHost(), null);

        Attendee attendee = new Attendee("attendee1");
        testEvent1.setHost(attendee);

        assertEquals(testEvent1.getHost(), attendee);
    }

    @Test
    public void updateRSVP_AddRSVP() {
        Event testEvent1 = new Event(EVENT1_NAME,
                EVENT1_DESCRIPTION,
                EVENT1_LOCATION,
                EVENT1_TIME,
                EVENT1_CAPACITY,
                null);

        assertEquals(testEvent1.getRSVPList(), Arrays.asList(""));

        testEvent1.setRSVPList(EVENT1_RSVPLIST);

        List<String> rsvpList = new ArrayList<String>();
        rsvpList.add("Tony");
        rsvpList.add("Levi");

        assertEquals(testEvent1.getRSVPList(), rsvpList);
    }
}
