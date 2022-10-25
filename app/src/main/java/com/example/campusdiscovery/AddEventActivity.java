package com.example.campusdiscovery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

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
        Intent data = new Intent();
        String text = "Result to be returned....";
        data.setData(Uri.parse(text));
        setResult(RESULT_OK, data);
        finish();
    }
}