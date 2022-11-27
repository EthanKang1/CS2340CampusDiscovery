package com.example.campusdiscovery;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.campusdiscovery.interfaces.EventListListener;
import com.example.campusdiscovery.interfaces.UpdateListener;
import com.example.campusdiscovery.models.Attendee;
import com.example.campusdiscovery.models.Event;
import com.example.campusdiscovery.models.EventListViewModel;
import com.example.campusdiscovery.models.UserMapViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.campusdiscovery.ui.main.SectionsPagerAdapter;
import com.example.campusdiscovery.databinding.ActivityEventNewBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EventActivityNew extends AppCompatActivity implements UpdateListener {

    // TODO: make this class handle global changes

    private List<Event> eventList = new ArrayList<Event>();

    private ActivityEventNewBinding binding;

    private EventListViewModel eventListViewModel;
    private UserMapViewModel userMapViewModel;

    private Gson gson = new Gson();

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

        // Initialize live data models
        this.eventListViewModel = new ViewModelProvider(this).get(EventListViewModel.class);
        this.userMapViewModel = new ViewModelProvider(this).get(UserMapViewModel.class);

        // Load data
        this.loadEventList();
        this.loadUserMap();
    }

    @Override
    public void notifyUpdate() {
        SharedPreferences sh = this.getSharedPreferences("EventsPref", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sh.edit();

        eventListViewModel.getSelectedItem().observe(this, item -> {
            String eventListJson = this.gson.toJson(item);
            prefsEditor.putString("events", eventListJson);
            prefsEditor.commit();
        });
    }

    /**
     * Loads saved event data from local storage into the activity's attributes.
     */
    private void loadEventList() {
        SharedPreferences sh = this.getSharedPreferences("EventsPref", MODE_APPEND);
        String eventsPref = sh.getString("events", "");

        if (eventsPref == "") {
            eventListViewModel.selectItem(new ArrayList<Event>());
        } else {
            eventListViewModel.selectItem(gson.fromJson(eventsPref, new TypeToken<List<Event>>() {}.getType()));
        }
    }

    /**
     * Loads saved event data from local storage into the activity's attributes.
     */
    private void loadUserMap() {
        SharedPreferences sh = this.getSharedPreferences("UsersPref", MODE_APPEND);
        String usersPref = sh.getString("users", "");

        Type userMapType = new TypeToken<Map<UUID, Attendee>>() {}.getType();
        userMapViewModel.selectItem(new HashMap<UUID, Attendee>());
        if (usersPref != "") {
            userMapViewModel.selectItem(gson.fromJson(usersPref, userMapType));
        }
    }
}