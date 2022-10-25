package com.example.campusdiscovery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class EventsAdapter extends ArrayAdapter<Event> {

    public EventsAdapter(Context context, List<Event> events) {

        super(context, 0, events);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
        }
        TextView eventName = (TextView) convertView.findViewById(R.id.eventName);
        eventName.setText(event.getName());
        TextView eventDescription = (TextView) convertView.findViewById(R.id.eventDescription);
        eventDescription.setText(event.getDescription());
        TextView eventTime = (TextView) convertView.findViewById(R.id.eventTime);
        eventTime.setText(event.getTime());
        TextView eventLocation = (TextView) convertView.findViewById(R.id.eventLocation);
        eventLocation.setText(event.getLocation());
        TextView eventHost = (TextView) convertView.findViewById(R.id.eventHost);
        eventHost.setText("Hosted by " + event.getHost());


        return convertView;

    }

}
