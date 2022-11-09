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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
    }

    public void backClick(View view) {
        finish();
    }

    public void submitClick(View view) {
        EditText eventTitleText = findViewById(R.id.eventTitleEditText);
        EditText eventDescriptionText = findViewById(R.id.eventDescriptionEditText);
        EditText eventLocationText = findViewById(R.id.eventLocationEditText);
        EditText eventTimeText = findViewById(R.id.eventTimeEditText);
        Intent data = new Intent();
        data.putExtra("action", "add");
        data.putExtra("eventTitle", eventTitleText.getText().toString());
        data.putExtra("eventDescription", eventDescriptionText.getText().toString());
        data.putExtra("eventLocation", eventLocationText.getText().toString());
        data.putExtra("eventTime", eventTimeText.getText().toString());
        setResult(RESULT_OK,data);
        finish();
    }
}