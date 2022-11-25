package com.example.campusdiscovery.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.campusdiscovery.R;
import com.example.campusdiscovery.adapters.EventsAdapter;
import com.example.campusdiscovery.interfaces.BtnClickListener;
import com.example.campusdiscovery.interfaces.SpinnerListener;
import com.example.campusdiscovery.models.Attendee;
import com.example.campusdiscovery.models.Event;
import com.example.campusdiscovery.models.Status;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserEventsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EventsAdapter eventsAdapter;

    private Attendee currentUser;

    private List<Event> pageEventList;

    public UserEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_events, container, false);


        // initialize event adapter
        this.eventsAdapter = new EventsAdapter(getActivity(), this.pageEventList, new BtnClickListener() {
            /**
             * Method that handles when a button is clicked on an event item.
             * @param position the position of the event
             * @param action the desired action of the mouse click
             */
            @Override
            public void onBtnClick(int position, String action) {
                if (action == "delete") {
                    deleteEvent(position);
                } else if (action == "edit") {
                    openEditEventActivity(position);
                }
            }
        }, new SpinnerListener() {
            /**
             * Method that captures an event change on the spinner (toggle) on an event item.
             * @param position the position of the event
             * @param status the resulting status clicked
             */
            @Override
            public void onItemSelect(int position, Status status) {
                editEventStatus(position, status);
            }
        }, this.currentUser);



        return view;
    }

    public void deleteEvent(int position) {
        // TODO
    }

    public void openEditEventActivity(int position) {
        // TODO
    }

    public void editEventStatus(int position, Status status) {
        // TODO
    }
}