package com.example.campusdiscovery.fragments;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.campusdiscovery.R;
import com.example.campusdiscovery.activities.EditEventActivity;
import com.example.campusdiscovery.activities.ViewEventActivity;
import com.example.campusdiscovery.adapters.EventsAdapter;
import com.example.campusdiscovery.interfaces.BtnClickListener;
import com.example.campusdiscovery.interfaces.SpinnerListener;
import com.example.campusdiscovery.interfaces.UpdateListener;
import com.example.campusdiscovery.models.Attendee;
import com.example.campusdiscovery.models.Event;
import com.example.campusdiscovery.models.EventListViewModel;
import com.example.campusdiscovery.models.Status;
import com.example.campusdiscovery.models.UserMapViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventListFragment extends Fragment {

    // UI elements
    private ListView eventListView;
    private LinearLayout paginationButtonLayout;

    // Button data
    private Button[] paginationButtons;
    private LinearLayout.LayoutParams paginationButtonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    private int numOfButtons;
    private int currentPage;
    private List<Event> pagedEventList = new ArrayList<Event>();

    // data
    private EventListViewModel eventListViewModel;
    private UserMapViewModel userMapViewModel;
    private Attendee currentUser;
    private String currentUserType;
    private Boolean isUserView;

    // Adapters
    private EventsAdapter eventsAdapter;

    // Listeners
    private UpdateListener updateListener;

    // CONSTANTS
    private static int NUM_ITEMS_PAGE = 10;
    private final ActivityResultLauncher<Intent> eventActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        Event newEvent = gson.fromJson(data.getStringExtra("currentEvent"), Event.class);
                        String action = data.getStringExtra("action");
                        String RSVPList = data.getStringExtra("RSVPList");
                        int eventPosition = data.getIntExtra("eventPosition", -1);

                        eventListViewModel.getSelectedItem().observe(requireActivity(), item -> {
                            item.set(eventPosition, newEvent);
                            updateListener.notifyUpdate();
                            eventsAdapter.notifyDataSetChanged();
                            loadEventPage();
                        });

                    }
                }
            }
    );

    // Gson
    private Gson gson = new Gson();

    public EventListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EventMapFragment.
     */
    public static EventListFragment newInstance() {
        EventListFragment fragment = new EventListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        // Load data from parent
        this.eventListViewModel = new ViewModelProvider(requireActivity()).get(EventListViewModel.class);
        this.userMapViewModel = new ViewModelProvider(requireActivity()).get(UserMapViewModel.class);
        this.currentUser = this.gson.fromJson(getArguments().getString("currentUser"), Attendee.class);
        this.currentUserType = getArguments().getString("currentUserType");
        this.isUserView = getArguments().getBoolean("userView");

        // Get UI elements
        this.eventListView = (ListView) view.findViewById(R.id.userEventsView);
        this.paginationButtonLayout = (LinearLayout) view.findViewById(R.id.btnLay);

        // Create pagination button footer
        initializeEventButtonFooter();

        // Initialize event adapter
        this.eventsAdapter = new EventsAdapter(getActivity(), this.pagedEventList, new BtnClickListener() {
            /**
             * Method that handles when a button is clicked on an event item.
             * @param position the position of the event
             * @param action the desired action of the mouse click
             */
            @Override
            public void onBtnClick(int position, String action) {
                if (action == "delete") {
                    eventListViewModel.getSelectedItem().observe(requireActivity(), item -> {
                        if (item.get(position).getHost().equals(currentUser) || currentUserType.equals("Organizer")) {
                            item.remove(position);
                            updateListener.notifyUpdate();
                            eventsAdapter.notifyDataSetChanged();

                            initializeEventButtonFooter();
                            loadEventPage();
                        } else {
                            Toast toast = Toast.makeText(getActivity(), "You are not an organizer or the owner of this event.", LENGTH_SHORT);
                            toast.show();

                        }
                    });
                } else if (action == "edit") {
                    Event currentEvent = pagedEventList.get(position);
                    String currentEventJson = gson.toJson(currentEvent);

                    Intent intent = new Intent(getActivity(), EditEventActivity.class);
                    intent.putExtra("currentEvent", currentEventJson);
                    intent.putExtra("eventPosition", position);
                    intent.putExtra("eventAttendeeMap", gson.toJson(currentEvent.getAttendeeMap()));
                    eventActivityResultLauncher.launch(intent);
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

        this.eventListView.setAdapter(this.eventsAdapter);
        this.eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            /**
             * Method that handles a click on an event listing itself.
             * @param adapter
             * @param v
             * @param position
             * @param arg3
             */
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                // This isn't a great implementation, but it works
                userMapViewModel.getSelectedItem().observe(requireActivity(), item -> {
                    Event currentEvent = pagedEventList.get(position);
                    String currentEventJson = gson.toJson(currentEvent);
                    String userMapJson = gson.toJson(item);

                    Intent intent = new Intent(getActivity(), ViewEventActivity.class);
                    intent.putExtra("currentEvent", currentEventJson);
                    intent.putExtra("userMap", userMapJson);
                    intent.putExtra("eventPosition", position);

                    eventActivityResultLauncher.launch(intent);
                });
            }
        });

        // Load first page
        this.loadEventPage();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        updateListener = (UpdateListener) context;
    }

    private void initializeEventButtonFooter()
    {
        this.eventListViewModel.getSelectedItem().observe(requireActivity(), item -> {
            this.numOfButtons = (int) Math.ceil((float) item.size() / NUM_ITEMS_PAGE);
            this.paginationButtons = new Button[this.numOfButtons];

            this.paginationButtonLayout.removeAllViews();

            for(int i = 0; i < this.numOfButtons;i++) {
                this.paginationButtons[i] = new Button(getActivity());
                this.paginationButtons[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                this.paginationButtons[i].setText(String.valueOf(i + 1));
                this.paginationButtonLayout.addView(this.paginationButtons[i], this.paginationButtonLayoutParams);

                final int j = i;
                this.paginationButtons[j].setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        loadEventPage(j);
                    }
                });
            }
        });
    }

    /**
     //     * Method that modifies the displayed events on the screen.
     //     * @param page the desired page to view
     //     */
    private void loadEventPage(int page)
    {
        this.currentPage = page;
        this.pagedEventList.clear();

        this.eventListViewModel.getSelectedItem().observe(requireActivity(), item -> {
            for (int i = 0; i < NUM_ITEMS_PAGE; i++) {
                if ((page * NUM_ITEMS_PAGE) + i < item.size()) {
                    if (this.isUserView) {
                        if (item.get((page * NUM_ITEMS_PAGE) + i).getAttendeeStatus(this.currentUser) != null && item.get((page * NUM_ITEMS_PAGE) + i).getAttendeeStatus(this.currentUser) == Status.ATTEND) {
                            this.pagedEventList.add(item.get((page * NUM_ITEMS_PAGE) + i));
                        }

                    } else {
                        this.pagedEventList.add(item.get((page * NUM_ITEMS_PAGE) + i));
                    }

                }
            }
        });

        for(int i = 0;i < this.numOfButtons; i++)  {
            if(i == page)
            {
                this.paginationButtons[page].setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.tech_gold, null));
                this.paginationButtons[i].setTextColor(getResources().getColor(android.R.color.white));
            }
            else
            {
                this.paginationButtons[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                this.paginationButtons[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }

        this.eventsAdapter.notifyDataSetChanged();
    }

    /**
     //     * Chained method, uses saved attribute if no page is specified.
     //     */
    private void loadEventPage() {
        this.loadEventPage(this.currentPage);
    }

    /**
     //     * Changes the status of an event
     //     * @param position the position of the target event
     //     * @param status the current desired status of the event
     //     */
    private void editEventStatus(int position, Status status) {
        this.eventListViewModel.getSelectedItem().observe(requireActivity(), item -> {
            if (item.get(position).getRSVPList().contains(this.currentUser.getName()) || item.get(position).getRSVPList().contains("")) {
                if (item.get(position).getAttendeeStatus(this.currentUser) == null || !item.get(position).getAttendeeStatus(this.currentUser).equals(status)) {
                    item.get(position).setAttendee(this.currentUser.getId(), status);
                    updateListener.notifyUpdate();
                    this.eventsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    //Would write a type of sorting function here in regards to eventlistview and event list.

}