package com.example.campusdiscovery.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.campusdiscovery.R;

public class LaunchActivity extends AppCompatActivity {

    /**
     * Initializes the new activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Launches LoginActivity when welcome button is clicked.
     * @param view
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}