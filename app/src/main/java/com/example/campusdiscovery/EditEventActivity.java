package com.example.campusdiscovery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditEventActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
    }



    public void backClick(View view) {
        finish();
    }

    public void saveClick(View view) {
        EditText eventTitleText = findViewById(R.id.newEventTitle);
        EditText eventDescriptionText = findViewById(R.id.newEventDescription);
        EditText eventLocationText = findViewById(R.id.newEventLocation);
        EditText eventTimeText = findViewById(R.id.newEventTime);

        Intent data = new Intent();
        data.putExtra("eventTitle", eventTitleText.getText().toString());
        data.putExtra("eventDescription", eventDescriptionText.getText().toString());
        data.putExtra("eventLocation", eventLocationText.getText().toString());
        data.putExtra("eventTime", eventTimeText.getText().toString());
        setResult(RESULT_OK,data);
        finish();
    }




}
