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

        // Get UI elements
        // text
        TextView eventName = (TextView) convertView.findViewById(R.id.attendeeName);
        TextView eventDescription = (TextView) convertView.findViewById(R.id.eventDescription);
        TextView eventTime = (TextView) convertView.findViewById(R.id.eventTime);
        TextView eventLocation = (TextView) convertView.findViewById(R.id.eventLocation);
        TextView eventHost = (TextView) convertView.findViewById(R.id.eventHost);
        TextView eventCapacity = (TextView) convertView.findViewById((R.id.eventCapacity));
        TextView eventAttendees = (TextView) convertView.findViewById(R.id.eventAttendees);
        // buttons
        Button deleteButton = (Button) convertView.findViewById(R.id.delete);
        Button editButton = (Button) convertView.findViewById(R.id.edit);
        // spinner
        Spinner statusSpinner = (Spinner) convertView.findViewById(R.id.statusSpinner);

        // Set default text values
        eventName.setText(event.getName());
        eventDescription.setText(event.getDescription());
        eventTime.setText(event.getTime());
        eventLocation.setText(event.getLocation());
        eventHost.setText("Hosted by " + event.getHost().getName());
        eventCapacity.setText(event.getCapacity());
        eventAttendees.setText(event.getAttendees());

        // Set interactive component functionality
        // edit button
        editButton.setTag(position);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClickListener != null)
                    mClickListener.onBtnClick((Integer) view.getTag(), "edit");
            }
        });
        // delete button
        deleteButton.setTag(position);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClickListener != null)
                    mClickListener.onBtnClick((Integer) view.getTag(), "delete");
            }
        });
        // status spinner
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_item, Status.getStrings());
        statusSpinner.setAdapter(statusAdapter);
        if (event.getAttendeeStatus(this.currentUser) != null) {
            statusSpinner.setSelection(statusAdapter.getPosition(event.getAttendeeStatus(this.currentUser).toString()));
        } else {
            statusSpinner.setSelection(statusAdapter.getPosition(NO_ATTEND.toString()));
        }
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
                spinnerListener.onItemSelect(position, Status.valueOf(statusSpinner.getItemAtPosition(arg2).toString()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });

        return convertView;
    }
}

