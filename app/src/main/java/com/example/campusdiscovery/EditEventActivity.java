package com.example.campusdiscovery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class EditEventActivity extends AppCompatActivity {
    private static int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.position = extras.getInt("eventPosition");
        }

        String eventTitle = extras.getString("eventTitle");
        String eventDescription = extras.getString("eventDescription");
        String eventLocation = extras.getString("eventLocation");
        String eventTime = extras.getString("eventTime");

        this.setValues(eventTitle, eventDescription, eventLocation, eventTime);
    }



    public void backClick(View view) {
        finish();
    }

    private void setValues(String eventTitle,
                           String eventDescription,
                           String eventLocation,
                           String eventTime) {

        EditText eventTitleText = findViewById(R.id.newEventTitleEditText);
        EditText eventDescriptionText = findViewById(R.id.newEventDescriptionEditText);
        EditText eventLocationText = findViewById(R.id.newEventLocationEditText);
        EditText eventTimeText = findViewById(R.id.newEventTimeEditText);

        eventTitleText.setText(eventTitle);
        eventDescriptionText.setText(eventDescription);
        eventLocationText.setText(eventLocation);
        eventTimeText.setText(eventTime);
    }

    public void submitClick(View view) {
        EditText eventTitleText = findViewById(R.id.newEventTitleEditText);
        EditText eventDescriptionText = findViewById(R.id.newEventDescriptionEditText);
        EditText eventLocationText = findViewById(R.id.newEventLocationEditText);
        EditText eventTimeText = findViewById(R.id.newEventTimeEditText);

        Intent data = new Intent();
        data.putExtra("action", "edit");
        data.putExtra("eventPosition", this.getPosition());
        data.putExtra("eventTitle", eventTitleText.getText().toString());
        data.putExtra("eventDescription", eventDescriptionText.getText().toString());
        data.putExtra("eventLocation", eventLocationText.getText().toString());
        data.putExtra("eventTime", eventTimeText.getText().toString());
        setResult(RESULT_OK,data);
        finish();
    }

    public static int getPosition() {
        return position;
    }
}
