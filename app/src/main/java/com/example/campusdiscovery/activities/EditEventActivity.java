package com.example.campusdiscovery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusdiscovery.R;

public class EditEventActivity extends AppCompatActivity {
    private String eventTitle;
    private String eventDescription;
    private String eventLocation;
    private String eventTime;
    private static int position;

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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.position = extras.getInt("eventPosition");
            this.setValues(extras.getString("eventTitle"),
                    extras.getString("eventDescription"),
                    extras.getString("eventLocation"),
                    extras.getString("eventTime"),
                    extras.getString("eventCapacity"));
        }
    }

    /**
     * Ends the activity with no result following a back click.
     * @param view Current view
     */
    public void backClick(View view) {
        finish();
    }

    /**
     * Helper function that takes in event details and sets the value of the EditText fields
     * accordingly.
     * @param eventTitle Title of the current event
     * @param eventDescription Description of the current event
     * @param eventLocation Location of the current event
     * @param eventTime Time of the current event
     */
    private void setValues(String eventTitle,
                           String eventDescription,
                           String eventLocation,
                           String eventTime,
                           String eventCapacity) {

        EditText eventTitleText = findViewById(R.id.newEventTitleEditText);
        EditText eventDescriptionText = findViewById(R.id.newEventDescriptionEditText);
        EditText eventLocationText = findViewById(R.id.newEventLocationEditText);
        EditText eventTimeText = findViewById(R.id.newEventTimeEditText);
        EditText eventCapacityText = findViewById(R.id.newEventCapacityText);

        eventTitleText.setText(eventTitle);
        eventDescriptionText.setText(eventDescription);
        eventLocationText.setText(eventLocation);
        eventTimeText.setText(eventTime);
        eventCapacityText.setText(eventCapacity);
    }

    /**
     * Ends the activity with a successful result following the summit button being clicked.
     * This captures the text in each EditText field and packages this in an intent which is then
     * returned.
     * @param view
     */
    public void submitClick(View view) {
        EditText eventTitleText = findViewById(R.id.newEventTitleEditText);
        EditText eventDescriptionText = findViewById(R.id.newEventDescriptionEditText);
        EditText eventLocationText = findViewById(R.id.newEventLocationEditText);
        EditText eventTimeText = findViewById(R.id.newEventTimeEditText);
        EditText eventCapacityText = findViewById(R.id.newEventCapacityText);

        Intent data = new Intent();
        data.putExtra("action", "edit");
        data.putExtra("eventPosition", this.getPosition());
        data.putExtra("eventTitle", eventTitleText.getText().toString());
        data.putExtra("eventDescription", eventDescriptionText.getText().toString());
        data.putExtra("eventLocation", eventLocationText.getText().toString());
        data.putExtra("eventTime", eventTimeText.getText().toString());
        data.putExtra("eventCapacity", eventCapacityText.getText().toString());
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
