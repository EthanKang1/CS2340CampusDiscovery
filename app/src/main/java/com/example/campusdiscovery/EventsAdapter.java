package com.example.campusdiscovery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends ArrayAdapter<Event> {

    List<Event> events;
    String userName;
    String userType;

    public EventsAdapter(Context context, List<Event> events, String userName, String userType) {
        super(context, 0, events);
        this.events = events;
        this.userName = userName;
        this.userType = userType;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Event getItem(int position){
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return events.indexOf(events.get(position));
    }

    static class ViewHolder {
        ImageView delete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
            holder.delete=(ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (event.getHost().equals(userName) || userType.equals("Teacher") || userType.equals("Organizer")) {
                    events.remove(position);
                    notifyDataSetChanged();
                }
            }
        });

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
