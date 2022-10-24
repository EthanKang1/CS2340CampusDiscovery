package com.example.campusdiscovery;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EventActivity extends AppCompatActivity {

    private String userName;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.setUserName(extras.getString("userName"));
            this.setUserType(extras.getString("userType"));
        }
    }

    private void setUserName(String userName) {
        this.userName = userName;
    }
    private void setUserType(String userType) {
        this.userType = userType;
    }


}
