package com.example.campusdiscovery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

public class EventActivity extends AppCompatActivity {

    private String userName;
    private String userType;

    private Events eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.setUserName(extras.getString("userName"));
            this.setUserType(extras.getString("userType"));
        }

    }

    public void openAddEventActivity(View view) {
        Intent intent = new Intent(this, AddEventActivity.class);
        startActivity(intent);
    }

//    public void printEvents(View view) {
//        System.out.println(this.eventList.toJson());
//    }
//
//    public void addEvents(View view) {
//        Event newEvent = new Event("someting");
//        this.addEvent(newEvent);
//    }

//    // should only be used on initialization
//    private void getEventsPref() {
//        SharedPreferences sh = getSharedPreferences("EventsPref", MODE_APPEND);
//        String eventsPref = sh.getString("events", "");
//
//        Gson gson = new Gson();
//        Events obj = gson.fromJson(eventsPref, Events.class);
//        setEventList(obj);
//    }

//    private void clearEventsPref() {
//        SharedPreferences sh = getSharedPreferences("EventsPref", MODE_APPEND);
//        SharedPreferences.Editor prefsEditor = sh.edit();
//        prefsEditor.clear();
//        prefsEditor.commit();
//    }
//
//    private void updateEventsPref() {
//        SharedPreferences sh = getSharedPreferences("EventsPref",MODE_PRIVATE);
//        SharedPreferences.Editor prefsEditor = sh.edit();
//        prefsEditor.putString("events", this.eventList.toJson());
//        prefsEditor.commit();
//    }

    private void setUserName(String userName) {
        this.userName = userName;
    }
    private void setUserType(String userType) {
        this.userType = userType;
    }
    private void setEventList(Events eventList) { this.eventList = eventList; }

//    private void addEvent(Event event) {
//        this.eventList.addEvent(event);
//        this.updateEventsPref();
//    }


}
