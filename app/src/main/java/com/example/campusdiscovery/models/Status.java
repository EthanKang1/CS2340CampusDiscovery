package com.example.campusdiscovery.models;

import java.util.ArrayList;
import java.util.List;

public enum Status {
    ATTEND, MAYBE, NO_ATTEND, NEMESIS;

    @Override
    public String toString() {
        switch(this) {
            case ATTEND:
                return "Will Attend";

            case MAYBE:
                return "Maybe";

            case NO_ATTEND:
                return "Won't Attend";

            case NEMESIS:
                return "I'm Your Nemesis";

            default:
                return null;
        }
    }

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
