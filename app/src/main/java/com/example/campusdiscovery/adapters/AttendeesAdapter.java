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

    private BtnClickListener mClickListener = null;
    private Context context;

    private final List<String> statuses = Arrays.asList("Will Attend", "Maybe", "Won't Attend", "I'm Your Nemesis");
    private final List<String> badStatuses = Arrays.asList("Won't Attend", "I'm Your Nemesis");

    public AttendeesAdapter(Context context, List<Attendee> attendees, BtnClickListener listener) {
        super(context, 0, attendees);
        this.context = context;
        mClickListener = (BtnClickListener) listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Attendee attendee = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
        }

        Button deleteButton = (Button) convertView.findViewById(R.id.delete);
        TextView eventName = (TextView) convertView.findViewById(R.id.attendeeName);
        eventName.setText(attendee.getName());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClickListener != null)
                    mClickListener.onBtnClick((Integer) view.getTag(), "delete");
            }
        });
        
        return convertView;
    }
}

