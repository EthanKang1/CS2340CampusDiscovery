package com.example.campusdiscovery.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.campusdiscovery.EventActivityNew;
import com.example.campusdiscovery.R;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /**
     * Initializes the new activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Spinner userTypes = findViewById(R.id.userType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.userTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypes.setAdapter(adapter);
        userTypes.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        return;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    return;
    }

    /**
     * Performs field validation and launches the EventActivityNew if successful.
     * @param view
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, EventActivityNew.class);

        EditText userNameText = findViewById(R.id.userName);
        Spinner userTypeSpinner = (Spinner) findViewById(R.id.userType);

        if (userNameText.getText().toString().trim().matches("")) {
            Toast.makeText(this, "Please provide a username", Toast.LENGTH_SHORT).show();
            return;
        }

        intent.putExtra("userName", userNameText.getText().toString());
        intent.putExtra("userType", userTypeSpinner.getSelectedItem().toString());
        startActivity(intent);
    }
}