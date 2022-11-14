package com.example.campusdiscovery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusdiscovery.R;

public class ViewEventActivity extends AppCompatActivity {

    /**
     * Initializes the new activity.
     * Captures and sets the existing fields to the corresponding event data.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        Bundle extras = getIntent().getExtras();

        String eventTitle = extras.getString("eventTitle");
        String eventDescription = extras.getString("eventDescription");
        String eventLocation = extras.getString("eventLocation");
        String eventTime = extras.getString("eventTime");
        String eventCapacity = extras.getString("eventCapacity");
        String eventAttendees = extras.getString("eventAttendees");

        TextView eventTitleText = findViewById(R.id.eventTitle);
        TextView eventDescriptionText = findViewById(R.id.eventDescription);
        TextView eventLocationText = findViewById(R.id.eventLocation);
        TextView eventTimeText = findViewById(R.id.eventTime);
        TextView eventCapacityText = findViewById(R.id.eventCapacity);
        TextView eventAttendeesText = findViewById(R.id.eventAttendees);

        eventTitleText.setText(eventTitle);
        eventDescriptionText.setText(eventDescription);
        eventLocationText.setText(eventLocation);
        eventTimeText.setText(eventTime);
        eventCapacityText.setText(eventCapacity);
        eventAttendeesText.setText(eventAttendees);
    }

    /**
     * Ends the activity with no result following a back click.
     * @param view Current view
     */
    public void backClick(View view) {
        finish();
    }


}
