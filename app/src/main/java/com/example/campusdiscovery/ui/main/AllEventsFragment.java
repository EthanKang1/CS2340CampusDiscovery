package com.example.campusdiscovery.ui.main;

import static android.content.Context.MODE_PRIVATE;

import static com.example.campusdiscovery.models.Status.NO_ATTEND;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.example.campusdiscovery.R;
import com.example.campusdiscovery.activities.AddEventActivity;
import com.example.campusdiscovery.adapters.EventsAdapter;
import com.example.campusdiscovery.fragments.EventListFragment;
import com.example.campusdiscovery.fragments.EventMapFragment;
import com.example.campusdiscovery.interfaces.UpdateListener;
import com.example.campusdiscovery.models.Attendee;
import com.example.campusdiscovery.models.Event;
import com.example.campusdiscovery.models.EventListViewModel;
import com.example.campusdiscovery.models.UserMapViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
    private String currentUserType;

    // gson
    private Gson gson = new Gson();

    // current user information
    private String userName;
    private String userType;
    private UUID userId;

    // global user database
    private Map<UUID, Attendee> userMap;

    // pagination variables and elements
    private ListView eventListView;
    private EventsAdapter eventsAdapter;
    private int currentPage = 0;

    // event variables
    private List<Event> eventList = new ArrayList<Event>();
    private List<Event> pageEventList = new ArrayList<Event>();


    // NEW UPDATE
    // UI elements
    private Switch mapViewSwitch;
    private FloatingActionButton floatingAddEventButton;

    // data
    private EventListViewModel eventListViewModel;
    private UserMapViewModel userMapViewModel;

    // listeners
    private UpdateListener updateListener;

    // Launcher
    private final ActivityResultLauncher<Intent> eventActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        Event newEvent = gson.fromJson(data.getStringExtra("currentEvent"), Event.class);
                        String action = data.getStringExtra("action");
//                        String RSVPList = data.getStringExtra("RSVPList");

                        newEvent.setAttendee(currentUser.getId(), NO_ATTEND);
                        newEvent.setHost(currentUser);

                        eventListViewModel.getSelectedItem().observe(requireActivity(), item -> {
                            item.add(newEvent);
                            updateListener.notifyUpdate();
                            switchEventsView(new EventListFragment());
                        });
                    }
                }
            }
    );

    public AllEventsFragment() {
        // Required empty public constructor
    }

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
        this.currentUserType = getArguments().getString("currentUserType");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_events, container, false);

        // get UI elements
        this.mapViewSwitch = (Switch) view.findViewById(R.id.mapViewToggle);
        this.floatingAddEventButton = (FloatingActionButton) view.findViewById(R.id.addEventButton);

        // Load data
        this.eventListViewModel = new ViewModelProvider(requireActivity()).get(EventListViewModel.class);
        this.userMapViewModel = new ViewModelProvider(requireActivity()).get(UserMapViewModel.class);

        // bind listeners
        this.mapViewSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchEventsView(new EventMapFragment());
                } else {
                    switchEventsView(new EventListFragment());
                }
            }
        });
        this.floatingAddEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), AddEventActivity.class);
                eventActivityResultLauncher.launch(intent);
            }
        });

        // Open Event List by default
        switchEventsView(new EventListFragment());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.updateListener = (UpdateListener) context;
    }

    private void switchEventsView(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("currentUser", gson.toJson(this.currentUser));
        bundle.putString("currentUserType", this.currentUserType);
        bundle.putBoolean("userView", false);
        fragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction().replace(R.id.eventViewFrame, fragment).addToBackStack(null).commit();
    }
}