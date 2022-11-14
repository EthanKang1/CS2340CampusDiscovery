package com.example.campusdiscovery.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.campusdiscovery.R;

public class AddEventActivity extends AppCompatActivity {
    /**
     * Initializes the new activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
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
        EditText eventTitleText = findViewById(R.id.eventTitleEditText);
        EditText eventDescriptionText = findViewById(R.id.eventDescriptionEditText);
        EditText eventLocationText = findViewById(R.id.eventLocationEditText);
        EditText eventTimeText = findViewById(R.id.eventTimeEditText);
        EditText eventCapacityText = findViewById(R.id.eventCapacityText);
        EditText eventRSVPList = findViewById(R.id.RSVPList);

        Intent data = new Intent();
        data.putExtra("action", "add");
        data.putExtra("eventTitle", eventTitleText.getText().toString());
        data.putExtra("eventDescription", eventDescriptionText.getText().toString());
        data.putExtra("eventLocation", eventLocationText.getText().toString());
        data.putExtra("eventTime", eventTimeText.getText().toString());
        data.putExtra("eventCapacity", eventCapacityText.getText().toString());
        data.putExtra("RSVPList", eventRSVPList.getText().toString());
        setResult(RESULT_OK,data);
        finish();
    }
}