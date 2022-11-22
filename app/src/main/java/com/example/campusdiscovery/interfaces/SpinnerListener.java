package com.example.campusdiscovery.interfaces;

import com.example.campusdiscovery.models.Status;

public interface SpinnerListener {
    public abstract void onItemSelect(int position, Status status);
}
