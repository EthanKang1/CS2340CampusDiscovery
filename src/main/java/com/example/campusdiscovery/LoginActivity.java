package com.example.campusdiscovery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.metrics.Event;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
        String msg = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, EventActivity.class);

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