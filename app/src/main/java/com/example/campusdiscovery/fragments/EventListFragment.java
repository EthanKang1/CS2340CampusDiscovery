package com.example.campusdiscovery.fragments;

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
                    this.pagedEventList.add(item.get((page * NUM_ITEMS_PAGE) + i));
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
}





//package com.example.campusdiscovery.fragments;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.activity.result.ActivityResult;
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.core.content.res.ResourcesCompat;
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//
//import com.example.campusdiscovery.R;
//import com.example.campusdiscovery.activities.AddEventActivity;
//import com.example.campusdiscovery.activities.EditEventActivity;
//import com.example.campusdiscovery.activities.ViewEventActivity;
//import com.example.campusdiscovery.adapters.EventsAdapter;
//import com.example.campusdiscovery.interfaces.BtnClickListener;
//import com.example.campusdiscovery.interfaces.SpinnerListener;
//import com.example.campusdiscovery.models.Event;
//import com.example.campusdiscovery.models.Status;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link EventListFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class EventListFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    //
//    private int noOfBtns;
//    private Button[] btns;
//    private LinearLayout paginationButtonLayout;
//
//    // constants
//    private int NUM_ITEMS_PAGE = 10;
//    private LinearLayout.LayoutParams paginationButtonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//    private final ActivityResultLauncher<Intent> eventActivityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        Intent data = result.getData();
//
//                        Event newEvent = gson.fromJson(data.getStringExtra("currentEvent"), Event.class);
//                        String action = data.getStringExtra("action");
//                        String RSVPList = data.getStringExtra("RSVPList");
//                        int eventPosition = data.getIntExtra("eventPosition", -1);
//
//                        if (action.equals("add")) {
//                            addEvent(newEvent);
//                        } else if (action.equals("edit")) {
//                            editEvent(eventPosition, newEvent);
//                        }
//                    }
//                }
//            }
//    );
//
//    public EventListFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment EventListFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static EventListFragment newInstance(String param1, String param2) {
//        EventListFragment fragment = new EventListFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//
//        // creates event pagination button footer
//        initializeEventButtonFooter();
//
//        // initialize event adapter
//        this.eventsAdapter = new EventsAdapter(getActivity(), this.pageEventList, new BtnClickListener() {
//            /**
//             * Method that handles when a button is clicked on an event item.
//             * @param position the position of the event
//             * @param action the desired action of the mouse click
//             */
//            @Override
//            public void onBtnClick(int position, String action) {
//                if (action == "delete") {
//                    deleteEvent(position);
//                } else if (action == "edit") {
//                    openEditEventActivity(position);
//                }
//            }
//        }, new SpinnerListener() {
//            /**
//             * Method that captures an event change on the spinner (toggle) on an event item.
//             * @param position the position of the event
//             * @param status the resulting status clicked
//             */
//            @Override
//            public void onItemSelect(int position, Status status) {
//                editEventStatus(position, status);
//            }
//        }, this.currentUser);
//
//        this.eventListView.setAdapter(this.eventsAdapter);
//        this.eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            /**
//             * Method that handles a click on an event listing itself.
//             * @param adapter
//             * @param v
//             * @param position
//             * @param arg3
//             */
//            @Override
//            public void onItemClick(AdapterView<?> adapter, View v, int position,
//                                    long arg3)
//            {
//                openViewEventActivity(position);
//            }
//        });
//
//        // loads first event page
//        this.loadEventPage();
//
//        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.addEventButton);
//        button.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(getActivity(), AddEventActivity.class);
//                eventActivityResultLauncher.launch(intent);
//            }
//        });
//
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_event_list, container, false);
//    }
//
//
//
//    /**
//     * Method that modifies the displayed events on the screen.
//     * @param page the desired page to view
//     */
//    private void loadEventPage(int page)
//    {
//        this.currentPage = page;
//        this.pageEventList.clear();
//
//        for (int i = 0; i < NUM_ITEMS_PAGE ; i++)  {
//            if ((page * NUM_ITEMS_PAGE) + i < this.eventList.size()) {
//                this.pageEventList.add(this.eventList.get((page * NUM_ITEMS_PAGE) + i));
//            }
//        }
//
//        for(int i = 0;i < this.noOfBtns; i++)
//        {
//            if(i == page)
//            {
//                btns[page].setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.tech_gold, null));
//                btns[i].setTextColor(getResources().getColor(android.R.color.white));
//            }
//            else
//            {
//                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
//                btns[i].setTextColor(getResources().getColor(android.R.color.black));
//            }
//        }
//
//        this.eventsAdapter.notifyDataSetChanged();
//    }
//
//    /**
//     * Chained method, uses saved attribute if no page is specified.
//     */
//    private void loadEventPage() {
//        this.loadEventPage(this.currentPage);
//    }
//
//    /**
//     * Removes an event given its position, saves this updated list, and calls for a refresh of the
//     * events screen
//     * @param position the position of the event to remove
//     */
//    private void deleteEvent(int position) {
//        if (this.eventList.get(position).getHost().equals(this.currentUser) || this.userType.equals("Organizer")) {
//            this.eventList.remove(position);
//            this.updateEventsPref();
//            this.eventsAdapter.notifyDataSetChanged();
//
////            this.initializeEventButtonFooter();
//            this.loadEventPage();
//        }
//    }
//
//    /**
//     * Replaces an existing event with a new Event object.
//     * @param position the position of the event to replace
//     * @param event the event to replace the previous event
//     */
//    private void editEvent(int position, Event event) {
//        this.eventList.set(position, event);
//        this.updateEventsPref();
//        this.eventsAdapter.notifyDataSetChanged();
//        System.out.println("event edited");
//        this.loadEventPage();
//    }
//
//    /**
//     * Changes the status of an event
//     * @param position the position of the target event
//     * @param status the current desired status of the event
//     */
//    private void editEventStatus(int position, Status status) {
//        if (eventList.get(position).getRSVPList().contains(userName) || eventList.get(position).getRSVPList().contains("")) {
//            if (this.eventList.get(position).getAttendeeStatus(this.currentUser) == null || !this.eventList.get(position).getAttendeeStatus(this.currentUser).equals(status)) {
//                this.eventList.get(position).setAttendee(this.currentUser.getId(), status);
//                this.updateEventsPref();
//                this.eventsAdapter.notifyDataSetChanged();
//                System.out.println("status edited");
//            }
//        }
//    }
//
//    /**
//     * A method that launches the EditEventActivity screen while passing in relevant event
//     * information.
//     * @param position the position of the current event
//     */
//    public void openEditEventActivity(int position){
//        Event currentEvent = this.eventList.get(position);
//        String currentEventJson = gson.toJson(currentEvent);
//
//        Intent intent = new Intent(getActivity(), EditEventActivity.class);
//        intent.putExtra("currentEvent", currentEventJson);
//        intent.putExtra("eventPosition", position);
//        intent.putExtra("eventAttendeeMap", this.gson.toJson(currentEvent.getAttendeeMap()));
//        eventActivityResultLauncher.launch(intent);
//    }
//
//    /**
//     * A method that opens the ViewEventActivity for a given activiy given its position
//     * @param position the position of the target activity
//     */
//    public void openViewEventActivity(int position) {
//        Event currentEvent = this.eventList.get(position);
//        String currentEventJson = gson.toJson(currentEvent);
//        String userMapJson = gson.toJson(this.userMap);
//
//        Intent intent = new Intent(getActivity(), ViewEventActivity.class);
//        intent.putExtra("currentEvent", currentEventJson);
//        intent.putExtra("userMap", userMapJson);
//        intent.putExtra("eventPosition", position);
//        eventActivityResultLauncher.launch(intent);
//    }
//}