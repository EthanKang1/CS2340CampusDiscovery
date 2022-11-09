package com.example.campusdiscovery.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.campusdiscovery.BtnClickListener;
import com.example.campusdiscovery.Event;
import com.example.campusdiscovery.EventsAdapter;
import com.example.campusdiscovery.R;
import com.example.campusdiscovery.SpinnerListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity{

    // current user information
    private String userName;
    private String userType;

    // pagination variables and elements
    private int noOfBtns;
    private Button[] btns;
    private LinearLayout paginationButtonLayout;
    private ListView eventListView;
    private EventsAdapter eventsAdapter;
    private int currentPage = 0;

    // event variables
    private List<Event> eventList = new ArrayList<Event>();
    private List<Event> pageEventList = new ArrayList<Event>();

    // constants
    private int NUM_ITEMS_PAGE = 10;
    private LinearLayout.LayoutParams paginationButtonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    private final ActivityResultLauncher<Intent> eventActivityResultLauncher = registerForActivityResult(
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
                    String action = data.getStringExtra("action");
                    int eventPosition = data.getIntExtra("eventPosition", -1);

                    Event newEvent = new Event(eventTitle, eventDescription, eventLocation, eventTime, getUserName());
                    if (action.equals("add")) {
                        addEvent(newEvent);
                    } else if (action.equals("edit")) {
                        editEvent(eventPosition, newEvent);
                    }
                }
            }
        }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // stores visual element variables
        this.eventListView = (ListView) findViewById(R.id.lvItems);
        this.paginationButtonLayout = (LinearLayout) findViewById(R.id.btnLay);

        // gets user data from login screen
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.setUserName(extras.getString("userName"));
            this.setUserType(extras.getString("userType"));
        }

        // loads event data from storage
        this.loadEventsPref();

        // creates event pagination button footer
        initializeEventButtonFooter();

        // initialize event adapter
        this.eventsAdapter = new EventsAdapter(this, this.pageEventList, new BtnClickListener() {
            @Override
            public void onBtnClick(int position, String action) {
                if (action == "delete") {
                    deleteEvent(position);
                } else if (action == "edit") {
                    openEditEventActivity(position);
                }
            }
        }, new SpinnerListener() {
            @Override
            public void onItemSelect(int position, int status) {
                System.out.println(position);
                System.out.println(status);
                editEventStatus(position, status);
            }
        }, getUserName());

        this.eventListView.setAdapter(this.eventsAdapter);
        this.eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                openViewEventActivity(position);
            }
        });

        // loads first event page
        this.loadEventPage();
    }

    public void openEditEventActivity(int position){
        System.out.println("Correct launch");
        Event currentEvent = this.eventList.get(position);
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("eventTitle", currentEvent.getName());
        intent.putExtra("eventDescription", currentEvent.getDescription());
        intent.putExtra("eventLocation", currentEvent.getLocation());
        intent.putExtra("eventTime", currentEvent.getTime());
        intent.putExtra("eventPosition", position);
        eventActivityResultLauncher.launch(intent);
    }

    public void openAddEventActivity(View view) {

        Intent intent = new Intent(this, AddEventActivity.class);
        eventActivityResultLauncher.launch(intent);
    }

    public void openViewEventActivity(int position) {
        System.out.println("Correct launch");
        Event currentEvent = this.eventList.get(position);
        Intent intent = new Intent(this, ViewEventActivity.class);
        intent.putExtra("eventTitle", currentEvent.getName());
        intent.putExtra("eventDescription", currentEvent.getDescription());
        intent.putExtra("eventLocation", currentEvent.getLocation());
        intent.putExtra("eventTime", currentEvent.getTime());
        intent.putExtra("eventPosition", position);
        eventActivityResultLauncher.launch(intent);
    }

    private void addEvent(Event event) {
        this.eventList.add(event);
        this.updateEventsPref();
        this.eventsAdapter.notifyDataSetChanged();
        System.out.println("event added");

        this.initializeEventButtonFooter();
        this.loadEventPage(0);
    }

    private void deleteEvent(int position) {
        if (this.eventList.get(position).getHost().equals(this.userName) || this.userType.equals("Organizer")) {
            this.eventList.remove(position);
            this.updateEventsPref();
            this.eventsAdapter.notifyDataSetChanged();

            this.initializeEventButtonFooter();
            this.loadEventPage();
        }
    }

    private void editEvent(int position, Event event) {
        this.eventList.set(position, event);
        this.updateEventsPref();
        this.eventsAdapter.notifyDataSetChanged();
        System.out.println("event edited");
        this.loadEventPage();
    }

    private void editEventStatus(int position, int status) {
        this.eventList.get(position).setStatus(getUserName(), status);
        this.updateEventsPref();
        System.out.println("status edited");
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

    private void loadEventPage(int page)
    {
        this.currentPage = page;
        this.pageEventList.clear();

        for (int i = 0; i < NUM_ITEMS_PAGE ; i++)  {
            if ((page * NUM_ITEMS_PAGE) + i < this.eventList.size()) {
                this.pageEventList.add(this.eventList.get((page * NUM_ITEMS_PAGE) + i));
            }
        }

        for(int i = 0;i < this.noOfBtns; i++)
        {
            if(i == page)
            {
                btns[page].setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.tech_gold, null));
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
            }
            else
            {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }

        this.eventsAdapter.notifyDataSetChanged();
    }

    private void loadEventPage() {
        this.loadEventPage(this.currentPage);
    }

    private void initializeEventButtonFooter()
    {
        this.noOfBtns = (int) Math.ceil((float) this.eventList.size() / NUM_ITEMS_PAGE);
        this.btns = new Button[this.noOfBtns];

        this.paginationButtonLayout.removeAllViews();

        for(int i = 0; i < this.noOfBtns;i++) {
            // create page button
            this.btns[i] = new Button(this);
            this.btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            this.btns[i].setText(String.valueOf(i + 1));
            this.paginationButtonLayout.addView(btns[i], this.paginationButtonLayoutParams);

            // create button click handler
            final int j = i;
            btns[j].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    loadEventPage(j);
                }
            });
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
