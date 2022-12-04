package com.example.campusdiscovery.models;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class StatusUnitTest {
    @Test
    public void getStringsTest() {
        List<String> outputList = new ArrayList<String>();
        outputList.add(Status.ATTEND.toString());
        outputList.add(Status.MAYBE.toString());
        outputList.add(Status.NO_ATTEND.toString());
        outputList.add(Status.NEMESIS.toString());

        assertEquals(outputList, Status.getStrings());
    }
}
