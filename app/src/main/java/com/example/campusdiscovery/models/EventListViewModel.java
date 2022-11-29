package com.example.campusdiscovery.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

//Have to change the list of events before being selected by the eventListView.
//Or figure a way to pick out the items in order.

public class EventListViewModel extends ViewModel {

    //Load the event list.
    //Sorting here.
    //Notify the adapter has been changed in order to update the list.

    private final MutableLiveData<List<Event>> selectedItem = new MutableLiveData<List<Event>>();
    public void selectItem(List<Event> item) {
        selectedItem.setValue(item);
    }
    public LiveData<List<Event>> getSelectedItem() {
        return selectedItem;
    }

}

