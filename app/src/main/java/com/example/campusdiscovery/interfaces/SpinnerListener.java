package com.example.campusdiscovery.interfaces;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.example.campusdiscovery.models.Status;

public interface SpinnerListener {
    public abstract void onItemSelect(int position, Status status);
}
