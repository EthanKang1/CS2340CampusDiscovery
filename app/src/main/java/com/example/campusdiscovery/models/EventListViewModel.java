package com.example.campusdiscovery.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class EventListViewModel extends ViewModel {
    private final MutableLiveData<List<Event>> selectedItem = new MutableLiveData<List<Event>>();

    public void selectItem(List<Event> item) {
        selectedItem.setValue(item);
    }
    public LiveData<List<Event>> getSelectedItem() {
        return selectedItem;
    }
}

