package com.example.campusdiscovery.adapters;

import static com.example.campusdiscovery.models.Status.NO_ATTEND;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.campusdiscovery.interfaces.BtnClickListener;
import com.example.campusdiscovery.models.Attendee;
import com.example.campusdiscovery.models.Event;
import com.example.campusdiscovery.R;
import com.example.campusdiscovery.interfaces.SpinnerListener;
import com.example.campusdiscovery.models.Status;

import java.util.Arrays;
import java.util.List;

public class EventsAdapter extends ArrayAdapter<Event>{

    private BtnClickListener mClickListener = null;
    private SpinnerListener spinnerListener = null;
    List<Event> events;
    private Context context;
    private Attendee currentUser;

    private final List<String> badStatuses = Arrays.asList("Won't Attend", "I'm Your Nemesis");

    public EventsAdapter(Context context, List<Event> events, BtnClickListener listener, SpinnerListener spinnerListener, Attendee currentUser) {
        super(context, 0, events);
        this.context = context;
        mClickListener = (BtnClickListener) listener;
        this.spinnerListener = (SpinnerListener) spinnerListener;
        this.currentUser = currentUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);

        }
        Button deleteButton = (Button) convertView.findViewById(R.id.delete);
        Button editButton = (Button) convertView.findViewById(R.id.edit);
        TextView eventName = (TextView) convertView.findViewById(R.id.attendeeName);
        eventName.setText(event.getName());
        TextView eventDescription = (TextView) convertView.findViewById(R.id.eventDescription);
        eventDescription.setText(event.getDescription());
        TextView eventTime = (TextView) convertView.findViewById(R.id.eventTime);
        eventTime.setText(event.getTime());
        TextView eventLocation = (TextView) convertView.findViewById(R.id.eventLocation);
        eventLocation.setText(event.getLocation());
        TextView eventHost = (TextView) convertView.findViewById(R.id.eventHost);
        eventHost.setText("Hosted by " + event.getHost());
        TextView eventCapacity = (TextView) convertView.findViewById((R.id.eventCapacity));
        eventCapacity.setText(event.getCapacity());
        TextView eventAttendees = (TextView) convertView.findViewById(R.id.eventAttendees);
        eventAttendees.setText(event.getAttendees());
        deleteButton.setTag(position);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClickListener != null)
                    mClickListener.onBtnClick((Integer) view.getTag(), "delete");
            }
        });

        editButton.setTag(position);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClickListener != null)
                    mClickListener.onBtnClick((Integer) view.getTag(), "edit");
            }
        });

        // Status dropdown
        Spinner statusSpinner = (Spinner) convertView.findViewById(R.id.statusSpinner);
//        statusSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_item, Status.getStrings());
        System.out.println("Setting status ");
        System.out.println(event.getAttendeeStatus(this.currentUser));

//        ArrayAdapter<String> badStatusAdapter = new ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_item, this.badStatuses);
//        if (event.getRSVPList().contains(this.username) || event.getRSVPList().contains("") || this.username.equals(event.getHost())) {
//            statusSpinner.setAdapter(statusAdapter);
//        } else {
//            statusSpinner.setAdapter(badStatusAdapter);
//        }
        statusSpinner.setAdapter(statusAdapter);
        Integer currentUserStatusPerEvent = event.getAttendeeStatus(this.currentUser);
        if (currentUserStatusPerEvent != null) {
            statusSpinner.setSelection(currentUserStatusPerEvent);
        } else {
            statusSpinner.setSelection(statusAdapter.getPosition(NO_ATTEND.toString()));
        }


//        statusSpinner.setSelection(event.getStatus(this.username));
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
//                System.out.println(position);
//                System.out.println(parent.getItemAtPosition(arg2).toString());
                spinnerListener.onItemSelect(position, arg2);


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        return convertView;
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        System.out.println(parent.getItemAtPosition(position).toString());
//        System.out.println(this.context);
////        this.spinnerListener.onItemSelect((Integer) view.getTag(), parent.getItemAtPosition(position).toString());
//    }
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) { }
}

