package com.example.campusdiscovery.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.campusdiscovery.R;
import com.example.campusdiscovery.adapters.EventsAdapter;
import com.example.campusdiscovery.fragments.EventListFragment;
import com.example.campusdiscovery.interfaces.BtnClickListener;
import com.example.campusdiscovery.interfaces.SpinnerListener;
import com.example.campusdiscovery.models.Attendee;
import com.example.campusdiscovery.models.Event;
import com.example.campusdiscovery.models.EventListViewModel;
import com.example.campusdiscovery.models.Status;
import com.example.campusdiscovery.models.UserMapViewModel;
import com.google.gson.Gson;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserEventsFragment extends Fragment {

    private EventsAdapter eventsAdapter;

    private Attendee currentUser;
    private String currentUserType;

    // data
    private EventListViewModel eventListViewModel;
    private UserMapViewModel userMapViewModel;

    private Gson gson = new Gson();

    public UserEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserEventsFragment newInstance(Bundle args) {
        UserEventsFragment fragment = new UserEventsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // parse current user
        this.currentUser = this.gson.fromJson(getArguments().getString("currentUser"), Attendee.class);
//        this.userMap = this.gson.fromJson(getArguments().getString("userMap"), userMapType);
        this.currentUserType = getArguments().getString("currentUserType");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_events, container, false);


        Fragment fragment = new EventListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("currentUser", gson.toJson(this.currentUser));
        bundle.putString("currentUserType", this.currentUserType);
        bundle.putBoolean("userView", true);
        fragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction().replace(R.id.userEventViewFrame, fragment).addToBackStack(null).commit();



        return view;
    }
}