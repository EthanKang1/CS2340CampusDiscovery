package com.example.campusdiscovery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class EventsAdapter extends ArrayAdapter<Event>{

    private BtnClickListener mClickListener = null;
    List<Event> events;


    public EventsAdapter(Context context, List<Event> events, BtnClickListener listener) {
        super(context, 0, events);
        mClickListener = (BtnClickListener) listener;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);

        }
        Button deleteButton = (Button) convertView.findViewById(R.id.delete);
        Button editButton = (Button) convertView.findViewById(R.id.edit);
        Button viewButton = (Button) convertView.findViewById(R.id.view);
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

        viewButton.setTag(position);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClickListener != null)
                    mClickListener.onBtnClick((Integer) view.getTag(), "view");
            }
        });

        return convertView;
    }
}

