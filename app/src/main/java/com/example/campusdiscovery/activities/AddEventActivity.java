package com.example.campusdiscovery.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.campusdiscovery.R;
import com.example.campusdiscovery.models.Event;
import com.google.gson.Gson;

public class AddEventActivity extends AppCompatActivity {

    // UI elements
    EditText eventTitleText;
    EditText eventDescriptionText;
    EditText eventLocationText;
    EditText eventTimeText;
    EditText eventCapacityText;
    EditText eventRSVPList;

    Gson gson = new Gson();

    /**
     * Initializes the new activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // UI elements
        this.eventTitleText = findViewById(R.id.eventTitleEditText);
        this.eventDescriptionText = findViewById(R.id.eventDescriptionEditText);
        this.eventLocationText = findViewById(R.id.eventLocationEditText);
        this.eventTimeText = findViewById(R.id.eventTimeEditText);
        this.eventCapacityText = findViewById(R.id.eventCapacityText);
        this.eventRSVPList = findViewById(R.id.RSVPList);
    }

    /**
     * Ends the activity with no result following a back click.
     * @param view Current view
     */
    public void backClick(View view) {
        finish();
    }

    /**
     * Ends the activity with a successful result following the summit button being clicked.
     * This captures the text in each EditText field and packages this in an intent which is then
     * returned.
     * @param view
     */
    public void submitClick(View view) {
        Event newEvent = new Event(eventTitleText.getText().toString(),
                eventDescriptionText.getText().toString(),
                eventLocationText.getText().toString(),
                eventTimeText.getText().toString(),
                Integer.parseInt("0" + eventCapacityText.getText().toString()),
                eventRSVPList.getText().toString()
        );

        Intent data = new Intent();
        data.putExtra("currentEvent", this.gson.toJson(newEvent));
        setResult(RESULT_OK,data);
        finish();
    }
}