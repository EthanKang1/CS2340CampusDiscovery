package com.example.campusdiscovery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusdiscovery.R;
import com.example.campusdiscovery.models.Event;
import com.google.gson.Gson;

public class EditEventActivity extends AppCompatActivity {
    private Event currentEvent;
    private static int position;

    // UI elements
    private EditText eventTitleText;
    private EditText eventDescriptionText;
    private EditText eventLocationText;
    private EditText eventTimeText;
    private EditText eventCapacityText;

    // gson
    Gson gson = new Gson();

    /**
     * Initializes the new activity.
     * Pulls the current event data from the the intent extras and sets the text fields to the
     * existing values.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        // Load arguments
        Bundle extras = getIntent().getExtras();
        this.currentEvent = this.gson.fromJson(extras.getString("currentEvent"), Event.class);
        this.position = extras.getInt("eventPosition");

        // Get UI elements
        this.eventTitleText = findViewById(R.id.newEventTitleEditText);
        this.eventDescriptionText = findViewById(R.id.newEventDescriptionEditText);
        this.eventLocationText = findViewById(R.id.newEventLocationEditText);
        this.eventTimeText = findViewById(R.id.newEventTimeEditText);
        this.eventCapacityText = findViewById(R.id.newEventCapacityText);

        // Set default text
        this.eventTitleText.setText(this.currentEvent.getName());
        this.eventDescriptionText.setText(this.currentEvent.getDescription());
        this.eventLocationText.setText(this.currentEvent.getLocation());
        this.eventTimeText.setText(this.currentEvent.getTime());
        this.eventCapacityText.setText(this.currentEvent.getCapacity());
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
        currentEvent.setName(this.eventTitleText.getText().toString());
        currentEvent.setDescription(this.eventDescriptionText.getText().toString());
        currentEvent.setLocation(this.eventLocationText.getText().toString());
        currentEvent.setTime(this.eventTimeText.getText().toString());
        currentEvent.setCapacity(this.eventCapacityText.getText().toString());

        Intent data = new Intent();

        data.putExtra("action", "edit");
        data.putExtra("currentEvent", this.gson.toJson(currentEvent));
        data.putExtra("eventPosition", this.getPosition());
        setResult(RESULT_OK,data);
        finish();
    }

    /**
     * Getter method for position attribute.
     * @return the position of the current event on the main screen
     */
    public static int getPosition() {
        return position;
    }
}
