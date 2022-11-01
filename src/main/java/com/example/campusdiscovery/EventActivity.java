package com.example.campusdiscovery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity {

    private String userName;
    private String userType;
    private List<Event> eventList = new ArrayList<Event>();
    private Pagination pagination;
    private int currentPage = 0;
    private ListView listView;
    private Button next, previous;
    EventsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        next = findViewById(R.id.bt_next);
        previous = findViewById(R.id.bt_prev);
        listView = findViewById(R.id.lvItems);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.setUserName(extras.getString("userName"));
            this.setUserType(extras.getString("userType"));
        }
        updateEventsPref();
        pagination = new Pagination(10, this.eventList);
        this.adapter= new EventsAdapter(this, this.eventList);
        listView.setAdapter(adapter);

    }

    public void openAddEventActivity(View view) {

        Intent intent = new Intent(this, AddEventActivity.class);
        someActivityResultLauncher.launch(intent);
    }

    public void incrementPage(View view) {
        currentPage += 1;
        updateEventsPref();
    }

    public void decrementPage(View view) {
        currentPage -= 1;
        updateEventsPref();
    }

    public void toggleEventSelection(View view) {
        if (findViewById(R.id.eventDescription).getVisibility() == View.INVISIBLE) {
            findViewById(R.id.eventDescription).setVisibility(View.VISIBLE);
            findViewById(R.id.eventTime).setVisibility(View.VISIBLE);
            findViewById(R.id.eventHost).setVisibility(View.VISIBLE);
            findViewById(R.id.eventLocation).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.eventDescription).setVisibility(View.INVISIBLE);
            findViewById(R.id.eventTime).setVisibility(View.INVISIBLE);
            findViewById(R.id.eventHost).setVisibility(View.INVISIBLE);
            findViewById(R.id.eventLocation).setVisibility(View.INVISIBLE);
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String eventTitle = data.getStringExtra("eventTitle");
                        String eventDescription = data.getStringExtra("eventDescription");
                        String eventLocation = data.getStringExtra("eventLocation");
                        String eventTime = data.getStringExtra("eventTime");
                        Event newEvent = new Event(eventTitle, eventDescription, eventLocation, eventTime, getUserName());
                        addEvent(newEvent);

                    }
                }
            });

    private void addEvent(Event event) {
        this.eventList.add(event);
        this.updateEventsPref();
        adapter.notifyDataSetChanged();
        System.out.println("event added");
    }

    private void updateEventsPref() {
        //listView.setAdapter(new ArrayAdapter<>(EventActivity.this, R.layout.activity_event, pagination.getEventData(currentPage)));
        updateButtons();

        SharedPreferences sh = getSharedPreferences("EventsPref",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sh.edit();
        Gson gson = new Gson();
        String menuJson = gson.toJson(this.eventList);
        System.out.println(menuJson);
        prefsEditor.putString("events", menuJson);
        prefsEditor.commit();
    }

    private void loadEventsPref() {
        SharedPreferences sh = getSharedPreferences("EventsPref", MODE_APPEND);
        String eventsPref = sh.getString("events", "");

        Gson gson = new Gson();
        Type eventListType = new TypeToken<List<Event>>() {}.getType();
        if (eventsPref == "") {
            this.eventList = new ArrayList<Event>();
        } else {
            this.eventList = gson.fromJson(eventsPref, eventListType);
        }

    }

    private void updateButtons() {
        if (currentPage == 0) {
            next.setEnabled(true);
            previous.setEnabled(false);
        } else if (currentPage == pagination.getLastPage()) {
            next.setEnabled(false);
            previous.setEnabled(true);
        } else {
            next.setEnabled(true);
            previous.setEnabled(true);
        }
    }

    private void setUserName(String userName) {
        this.userName = userName;
    }
    private void setUserType(String userType) {
        this.userType = userType;
    }

    private String getUserName() { return this.userName; }
    private String getUserType() { return this.userType; }

}
