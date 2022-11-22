package com.example.campusdiscovery.activities;

import static com.example.campusdiscovery.models.Status.ATTEND;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusdiscovery.R;
import com.example.campusdiscovery.adapters.AttendeesAdapter;
import com.example.campusdiscovery.databinding.ActivityViewEventBinding;
import com.example.campusdiscovery.interfaces.BtnClickListener;
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

    private ListView attendeeListView;
    private AttendeesAdapter attendeesAdapter;

    private Map<UUID, Integer> eventAttendeeMap = new HashMap<UUID, Integer>();
    private List<Attendee> attendeeList = new ArrayList<Attendee>();

    private Map<String, Integer> eventStatus;
    private Event currentEvent;

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
        this.currentEvent = gson.fromJson(extras.getString("currentEvent"), Event.class);

        // Get UI elements
        TextView eventTitleText = findViewById(R.id.eventTitle);
        TextView eventDescriptionText = findViewById(R.id.eventDescription);
        TextView eventLocationText = findViewById(R.id.eventLocation);
        TextView eventTimeText = findViewById(R.id.eventTime);
        TextView eventCapacityText = findViewById(R.id.eventCapacity);
        TextView eventAttendeesText = findViewById(R.id.eventAttendees);

        // Set default text values
        eventTitleText.setText(this.currentEvent.getName());
        eventDescriptionText.setText(this.currentEvent.getDescription());
        eventLocationText.setText(this.currentEvent.getLocation());
        eventTimeText.setText(this.currentEvent.getTime());
        eventCapacityText.setText(this.currentEvent.getCapacity());
        eventAttendeesText.setText(this.currentEvent.getAttendees());

        this.attendeeListView = (ListView) findViewById(R.id.attendeeListView);

        // initialize attendee adapter
        this.attendeesAdapter = new AttendeesAdapter(this, this.attendeeList, new BtnClickListener() {
            /**
             * Method that handles when a button is clicked on an event item.
             * @param position the position of the event
             * @param action the desired action of the mouse click
             */
            @Override
            public void onBtnClick(int position, String action) {
                System.out.println("position");
            }
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
        return;
    }


}
