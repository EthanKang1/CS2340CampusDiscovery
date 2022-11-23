package com.example.campusdiscovery;

import android.os.Bundle;

import com.example.campusdiscovery.interfaces.EventListListener;
import com.example.campusdiscovery.models.Attendee;
import com.example.campusdiscovery.models.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.campusdiscovery.ui.main.SectionsPagerAdapter;
import com.example.campusdiscovery.databinding.ActivityEventNewBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class EventActivityNew extends AppCompatActivity {

    // TODO: make this class handle global changes

    private List<Event> eventList = new ArrayList<Event>();

    private ActivityEventNewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEventNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), extras);
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
    }

    public void testContext() {
        System.out.println("Testing ocntext");
    }


}