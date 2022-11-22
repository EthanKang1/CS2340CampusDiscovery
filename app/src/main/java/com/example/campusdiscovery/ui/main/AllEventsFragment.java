package com.example.campusdiscovery.ui.main;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

import static com.example.campusdiscovery.models.Status.NO_ATTEND;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.campusdiscovery.R;
import com.example.campusdiscovery.activities.AddEventActivity;
import com.example.campusdiscovery.activities.EditEventActivity;
import com.example.campusdiscovery.activities.ViewEventActivity;
import com.example.campusdiscovery.adapters.EventsAdapter;
import com.example.campusdiscovery.interfaces.BtnClickListener;
import com.example.campusdiscovery.interfaces.SpinnerListener;
import com.example.campusdiscovery.models.Attendee;
import com.example.campusdiscovery.models.Event;
import com.example.campusdiscovery.models.Status;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllEventsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Attendee currentUser;

    // gson
    private Gson gson = new Gson();

    // current user information
    private String userName;
    private String userType;
    private UUID userId;

    // global user database
    private Map<UUID, Attendee> userMap;

    // pagination variables and elements
    private int noOfBtns;
    private Button[] btns;
    private LinearLayout paginationButtonLayout;
    private ListView eventListView;
    private EventsAdapter eventsAdapter;
    private int currentPage = 0;

    // event variables
    private List<Event> eventList = new ArrayList<Event>();
    private List<Event> pageEventList = new ArrayList<Event>();

    // constants
    private int NUM_ITEMS_PAGE = 10;
    private LinearLayout.LayoutParams paginationButtonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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

                        if (action.equals("add")) {
                            addEvent(newEvent);
                        } else if (action.equals("edit")) {
                            editEvent(eventPosition, newEvent);
                        }
                    }
                }
            }
    );



    public AllEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param args
     * @return A new instance of fragment AllEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllEventsFragment newInstance(Bundle args) {
        AllEventsFragment fragment = new AllEventsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Type userMapType = new TypeToken<Map<UUID, Attendee>>() {}.getType();

        // parse current user
        this.currentUser = this.gson.fromJson(getArguments().getString("currentUser"), Attendee.class);
        this.userMap = this.gson.fromJson(getArguments().getString("userMap"), userMapType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_events, container, false);

        // stores visual element variables
        this.eventListView = (ListView) view.findViewById(R.id.lvItems);
        this.paginationButtonLayout = (LinearLayout) view.findViewById(R.id.btnLay);

        // loads event data from storage
        this.loadEventsPref();

        // creates event pagination button footer
        initializeEventButtonFooter();

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
                                    long arg3)
            {
                openViewEventActivity(position);
            }
        });

        // loads first event page
        this.loadEventPage();

        Button button = (Button) view.findViewById(R.id.addEvent);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), AddEventActivity.class);
                eventActivityResultLauncher.launch(intent);
            }
        });

        return view;
    }

    /**
     * A method that launches the EditEventActivity screen while passing in relevant event
     * information.
     * @param position the position of the current event
     */
    public void openEditEventActivity(int position){
        Event currentEvent = this.eventList.get(position);
        String currentEventJson = gson.toJson(currentEvent);

        Intent intent = new Intent(getActivity(), EditEventActivity.class);
        intent.putExtra("currentEvent", currentEventJson);
        intent.putExtra("eventPosition", position);
        intent.putExtra("eventAttendeeMap", this.gson.toJson(currentEvent.getAttendeeMap()));
        eventActivityResultLauncher.launch(intent);
    }

    /**
     * A method that opens the ViewEventActivity for a given activiy given its position
     * @param position the position of the target activity
     */
    public void openViewEventActivity(int position) {
        Event currentEvent = this.eventList.get(position);
        String currentEventJson = gson.toJson(currentEvent);
        String userMapJson = gson.toJson(this.userMap);

        Intent intent = new Intent(getActivity(), ViewEventActivity.class);
        intent.putExtra("currentEvent", currentEventJson);
        intent.putExtra("userMap", userMapJson);
        intent.putExtra("eventPosition", position);
        eventActivityResultLauncher.launch(intent);
    }

    /**
     * Loads saved event data from local storage into the activity's attributes.
     */
    private void loadEventsPref() {
        SharedPreferences sh = getActivity().getSharedPreferences("EventsPref", MODE_APPEND);
        String eventsPref = sh.getString("events", "");

        Type eventListType = new TypeToken<List<Event>>() {}.getType();
        if (eventsPref == "") {
            this.eventList = new ArrayList<Event>();
        } else {
            this.eventList = gson.fromJson(eventsPref, eventListType);
        }

    }

    /**
     * Initializes the pagination buttons.
     */
    private void initializeEventButtonFooter()
    {
        this.noOfBtns = (int) Math.ceil((float) this.eventList.size() / NUM_ITEMS_PAGE);
        this.btns = new Button[this.noOfBtns];

        this.paginationButtonLayout.removeAllViews();

        for(int i = 0; i < this.noOfBtns;i++) {
            // create page button
            this.btns[i] = new Button(getActivity());
            this.btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            this.btns[i].setText(String.valueOf(i + 1));
            this.paginationButtonLayout.addView(btns[i], this.paginationButtonLayoutParams);

            // create button click handler
            final int j = i;
            btns[j].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    loadEventPage(j);
                }
            });
        }
    }

    /**
     * Method that modifies the displayed events on the screen.
     * @param page the desired page to view
     */
    private void loadEventPage(int page)
    {
        this.currentPage = page;
        this.pageEventList.clear();

        for (int i = 0; i < NUM_ITEMS_PAGE ; i++)  {
            if ((page * NUM_ITEMS_PAGE) + i < this.eventList.size()) {
                this.pageEventList.add(this.eventList.get((page * NUM_ITEMS_PAGE) + i));
            }
        }

        for(int i = 0;i < this.noOfBtns; i++)
        {
            if(i == page)
            {
                btns[page].setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.tech_gold, null));
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
            }
            else
            {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }

        this.eventsAdapter.notifyDataSetChanged();
    }

    /**
     * Chained method, uses saved attribute if no page is specified.
     */
    private void loadEventPage() {
        this.loadEventPage(this.currentPage);
    }

    /**
     * Adds an event to the activities attribute, saves this to data, and calls for a refresh of
     * the events screen.
     * @param event the event to add
     */
    private void addEvent(Event event) {
        // add current user attendee metadata (status and host)

        event.setAttendee(this.currentUser.getId(), NO_ATTEND);
        event.setHost(this.currentUser);

        this.eventList.add(event);
        this.updateEventsPref();
        this.eventsAdapter.notifyDataSetChanged();
        System.out.println("event added");

        this.initializeEventButtonFooter();
        this.loadEventPage(0);
    }

    /**
     * Removes an event given its position, saves this updated list, and calls for a refresh of the
     * events screen
     * @param position the position of the event to remove
     */
    private void deleteEvent(int position) {
        if (this.eventList.get(position).getHost().equals(this.currentUser) || this.userType.equals("Organizer")) {
            this.eventList.remove(position);
            this.updateEventsPref();
            this.eventsAdapter.notifyDataSetChanged();

            this.initializeEventButtonFooter();
            this.loadEventPage();
        }
    }

    /**
     * Replaces an existing event with a new Event object.
     * @param position the position of the event to replace
     * @param event the event to replace the previous event
     */
    private void editEvent(int position, Event event) {
        this.eventList.set(position, event);
        this.updateEventsPref();
        this.eventsAdapter.notifyDataSetChanged();
        System.out.println("event edited");
        this.loadEventPage();
    }

    /**
     * Changes the status of an event
     * @param position the position of the target event
     * @param status the current desired status of the event
     */
    private void editEventStatus(int position, Status status) {
        if (eventList.get(position).getRSVPList().contains(userName) || eventList.get(position).getRSVPList().contains("")) {
            this.eventList.get(position).setAttendee(this.currentUser.getId(), status);
            this.updateEventsPref();
            System.out.println("status edited");
        }
    }

    /**
     * Saves the activity's event list to local storage.
     */
    private void updateEventsPref() {
        SharedPreferences sh = getActivity().getSharedPreferences("EventsPref", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sh.edit();

        String menuJson = this.gson.toJson(this.eventList);
        System.out.println(menuJson);
        prefsEditor.putString("events", menuJson);
        prefsEditor.commit();
    }








    /**
     * Setter method for the username attribute.
     * @param userName the current username
     */
    private void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Setter method for the usertype attribute.
     * @param userType the current usertype
     */
    private void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * Getter method for the username attribute.
     * @return the current username
     */
    private String getUserName() { return this.userName; }

    /**
     * Getter method for the usertype attribute.
     * @return the current usertype
     */
    private String getUserType() { return this.userType; }
}