package com.example.campusdiscovery.activities;

import static com.example.campusdiscovery.models.Status.ATTEND;
import static com.example.campusdiscovery.models.Status.NO_ATTEND;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusdiscovery.R;
import com.example.campusdiscovery.adapters.AttendeesAdapter;
import com.example.campusdiscovery.databinding.ActivityViewEventBinding;
import com.example.campusdiscovery.interfaces.BtnClickListener;
import com.example.campusdiscovery.interfaces.SpinnerListener;
import com.example.campusdiscovery.models.Attendee;
import com.example.campusdiscovery.models.Event;
import com.example.campusdiscovery.models.Status;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ViewEventActivity extends AppCompatActivity {

    private ActivityViewEventBinding binding;


    private AttendeesAdapter attendeesAdapter;

    private Map<UUID, Integer> eventAttendeeMap = new HashMap<UUID, Integer>();

    private Map<String, Integer> eventStatus;
    private Event currentEvent;
    private Map<UUID, Attendee> userMap;

    // UI Elements
    private TextView eventTitleText;
    private TextView eventDescriptionText;
    private TextView eventLocationText;
    private TextView eventTimeText;
    private TextView eventCapacityText;
    private TextView eventAttendeesText;
    private ListView attendeeListView;
    private Spinner statusFilterSpinner;

    // handles types of status
    private List<Attendee> attendeeListPage = new ArrayList<Attendee>();

    /**
     * Initializes the new activity.
     * Captures and sets the existing fields to the corresponding event data.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        // Load arguments
        Gson gson = new Gson();
        Bundle extras = getIntent().getExtras();
        Type userMapType = new TypeToken<Map<UUID, Attendee>>() {}.getType();
        this.currentEvent = gson.fromJson(extras.getString("currentEvent"), Event.class);
        this.userMap = gson.fromJson(extras.getString("userMap"), userMapType);

        // Get UI elements
        this.eventTitleText = findViewById(R.id.eventTitle);
        this.eventDescriptionText = findViewById(R.id.eventDescription);
        this.eventLocationText = findViewById(R.id.eventLocation);
        this.eventTimeText = findViewById(R.id.eventTime);
        this.eventCapacityText = findViewById(R.id.eventCapacity);
        this.eventAttendeesText = findViewById(R.id.eventAttendees);
        this.attendeeListView = (ListView) findViewById(R.id.attendeeListView);
        this.statusFilterSpinner = (Spinner) findViewById(R.id.statusFilterSpinner);

        // Set default text values
        this.eventTitleText.setText(this.currentEvent.getName());
        this.eventDescriptionText.setText(this.currentEvent.getDescription());
        this.eventLocationText.setText(this.currentEvent.getLocation());
        this.eventTimeText.setText(this.currentEvent.getTime());
        this.eventCapacityText.setText(Integer.toString(this.currentEvent.getCapacity()));
        this.eventAttendeesText.setText(Integer.toString(this.currentEvent.getAttendeeCount()));

        // initialize attendee adapter
        this.attendeesAdapter = new AttendeesAdapter(this,
                                                    this.attendeeListPage);
        this.attendeeListView.setAdapter(attendeesAdapter);

        // spinner adapter
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Status.getStrings());
        this.statusFilterSpinner.setAdapter(statusAdapter);
        this.statusFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
                Status desiredStatus = Status.valueOf(statusFilterSpinner.getItemAtPosition(arg2).toString());
                loadAttendees(desiredStatus);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });

        this.loadAttendees(ATTEND);
    }

    /**
     * Ends the activity with no result following a back click.
     * @param view Current view
     */
    public void backClick(View view) {
        finish();
    }

    private void loadAttendees(Status status) {
        this.attendeeListPage.clear();
        System.out.println("Analayzing attendees");
        for (Map.Entry<UUID,Status> entry : this.currentEvent.getAttendeeMap().entrySet()) {
            UUID currentId = entry.getKey();
            Status currentStatus = entry.getValue();

            if (currentStatus.equals(status)) {
                this.attendeeListPage.add(this.userMap.get(currentId));
            }
        }

        this.attendeesAdapter.notifyDataSetChanged();
    }


}
