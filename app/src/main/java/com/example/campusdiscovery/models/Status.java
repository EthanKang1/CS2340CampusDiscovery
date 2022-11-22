package com.example.campusdiscovery.models;

import java.util.ArrayList;
import java.util.List;

public enum Status {
    ATTEND, MAYBE, NO_ATTEND, NEMESIS;

    public static List<String> getStrings() {
        // THIS IS BAD CODE, TODO
        List<String> outputArray = new ArrayList<String>();

        outputArray.add(ATTEND.toString());
        outputArray.add(MAYBE.toString());
        outputArray.add(NO_ATTEND.toString());
        outputArray.add(NEMESIS.toString());

        return outputArray;
    }
}
