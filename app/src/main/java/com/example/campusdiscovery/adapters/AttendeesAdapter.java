package com.example.campusdiscovery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.campusdiscovery.R;
import com.example.campusdiscovery.interfaces.BtnClickListener;
import com.example.campusdiscovery.models.Attendee;
import com.example.campusdiscovery.models.Event;

import java.util.Arrays;
import java.util.List;

public class AttendeesAdapter extends ArrayAdapter<Attendee>{

    private Context context;

    public AttendeesAdapter(Context context, List<Attendee> attendees) {
        super(context, 0, attendees);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Attendee attendee = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_attendee, parent, false);
        }

        // UI elements
        TextView attendeeName = (TextView) convertView.findViewById(R.id.attendeeName);
        Button deleteButton = (Button) convertView.findViewById(R.id.delete);

        // Set default text
        attendeeName.setText(attendee.getName());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("GOt attendee click");
            }
        });
        
        return convertView;
    }
}

