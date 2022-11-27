package com.example.campusdiscovery.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.campusdiscovery.EventActivityNew;
import com.example.campusdiscovery.R;
import com.example.campusdiscovery.models.Attendee;
import com.example.campusdiscovery.models.Event;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

        String userName = userNameText.getText().toString();
        String userType = userTypeSpinner.getSelectedItem().toString();

        if (userName.trim().matches("")) {
            Toast.makeText(this, "Please provide a username", Toast.LENGTH_SHORT).show();
            return;
        }

        // load user list
        SharedPreferences sh = this.getSharedPreferences("UsersPref", MODE_APPEND);
        String usersPref = sh.getString("users", "");
        Gson gson = new Gson();
        Type userMapType = new TypeToken<Map<UUID, Attendee>>() {}.getType();
        Map<UUID, Attendee> userMap = new HashMap<UUID, Attendee>();
        if (usersPref != "") {
            userMap = gson.fromJson(usersPref, userMapType);
        }

        // generate UUID if user does not exist
        Attendee currentUser = null;
        System.out.println("Comparing login: " + userName);
        for (Attendee user : userMap.values()) {
            System.out.println(user.getName());
            if (user.getName().equals(userName)) {
                System.out.println("MATCH");
                currentUser = user;
                break;
            }
        }
        if (currentUser == null) {
            System.out.println("User not found, creating...");
            currentUser = new Attendee(userName);

            userMap.put(currentUser.getId(), currentUser);
        }
        System.out.println(currentUser);

        // push user list
        sh = this.getSharedPreferences("UsersPref", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sh.edit();
        String userMapJson = gson.toJson(userMap);
        prefsEditor.putString("users", userMapJson);
        prefsEditor.commit();

        // Convert Attendee class to string
        String currentUserJson = gson.toJson(currentUser);

        // build arguments
        intent.putExtra("currentUser", currentUserJson);
        intent.putExtra("userMap", userMapJson);
        intent.putExtra("currentUserType", userType);

        // launch activity
        startActivity(intent);
    }
}