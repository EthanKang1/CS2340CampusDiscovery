package com.example.campusdiscovery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusdiscovery.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

public class FilterActivity extends AppCompatActivity {

    private Gson gson = new Gson();

    private boolean capAsc;

    private Button backButton;

    /**
     * Initializes the new activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        this.backButton = findViewById(R.id.button5);


        this.capAsc = false;
    }

    /**
     * Ends the activity with no result following a back click.
     * @param view Current view
     */
    public void backClick(View view) {
        Intent data = new Intent();

        data.putExtra("action", "filter");
        data.putExtra("result", capAsc);
        setResult(RESULT_OK,data);
        finish();
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.checkBoxCapacity:
                if (checked) {
                    capAsc = true;
                } else {
                    capAsc = false;
                }
        }
    }

}
