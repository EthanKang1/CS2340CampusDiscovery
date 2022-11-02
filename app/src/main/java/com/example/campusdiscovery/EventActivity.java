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
import android.widget.ImageView;
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

public class EventActivity extends AppCompatActivity implements BtnClickListener{

    private BtnClickListener mClickListener = null;

    private String userName;
    private String userType;

    private List<Event> eventList = new ArrayList<Event>();

    EventsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.setUserName(extras.getString("userName"));
            this.setUserType(extras.getString("userType"));
        }

        this.loadEventsPref();

        this.adapter = new EventsAdapter(this, this.eventList, new BtnClickListener() {
            @Override
            public void onBtnClick(int position) {
                deleteEvent(position);
            }
        });

        ListView listView = (ListView) findViewById(R.id.lvItems);
        listView.setAdapter(adapter);

    }

    public void openAddEventActivity(View view) {

        Intent intent = new Intent(this, AddEventActivity.class);
        someActivityResultLauncher.launch(intent);
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

    private void deleteEvent(int position) {
        if (this.eventList.get(position).getHost().equals(this.userName) || this.userType.equals("Organizer")) {
            this.eventList.remove(position);
            this.updateEventsPref();
            adapter.notifyDataSetChanged();
        }
    }


    private void updateEventsPref() {
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

    private void setUserName(String userName) {
        this.userName = userName;
    }
    private void setUserType(String userType) {
        this.userType = userType;
    }

    private String getUserName() { return this.userName; }
    private String getUserType() { return this.userType; }


    @Override
    public void onBtnClick(int position) {
        deleteEvent(position);
    }
}
