package com.example.campusdiscovery.models;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AttendeeUnitTest {

    @Test
    public void createAttendee_ValidInput() {
        String attendee1_name = "attendee1";
        Attendee attendee = new Attendee("attendee1");

        assertEquals(attendee1_name, attendee.getName());
    }

    @Test
    public void createAttendee_Unique() {
        Attendee attendee1 = new Attendee("attendee1");
        Attendee attendee2 = new Attendee("attendee2");

        assertNotEquals(attendee1.getId(), attendee2.getId());
    }

}
