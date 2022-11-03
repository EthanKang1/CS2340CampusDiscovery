package com.example.campusdiscovery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        Bundle extras = getIntent().getExtras();

        String eventTitle = extras.getString("eventTitle");
        String eventDescription = extras.getString("eventDescription");
        String eventLocation = extras.getString("eventLocation");
        String eventTime = extras.getString("eventTime");

        TextView eventTitleText = findViewById(R.id.eventTitle);
        TextView eventDescriptionText = findViewById(R.id.eventDescription);
        TextView eventLocationText = findViewById(R.id.eventLocation);
        TextView eventTimeText = findViewById(R.id.eventTime);

        eventTitleText.setText(eventTitle);
        eventDescriptionText.setText(eventDescription);
        eventLocationText.setText(eventLocation);
        eventTimeText.setText(eventTime);
    }

    public void backClick(View view) {
        finish();
    }


}
