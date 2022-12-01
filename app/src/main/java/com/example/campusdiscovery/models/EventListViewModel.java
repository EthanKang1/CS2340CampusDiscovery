package com.example.campusdiscovery.models;

import androidx.fragment.app.FragmentActivity;
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


    public void sortCapacityAsc(FragmentActivity context) {
        //Get the eventList somehow.

        this.getSelectedItem().observe(context, eventList -> {
            int i = 0;
            while (i < eventList.size() - 1 && eventList.get(i).getCapacity() > eventList.get(i + 1).getCapacity()) {
                if (eventList.get(i).getCapacity() > eventList.get(i + 1).getCapacity()) {
                    Event temp = eventList.get(i);
                    eventList.set(i, eventList.get(i+1));
                    eventList.set(i+1, temp);
                }
                i++;
            }
//            selectItem(eventList);
            //mutate the list
            //notifychange
        });

    }

    public void sortCapacityDesc(FragmentActivity context) {
        //Get the eventList somehow.

        this.getSelectedItem().observe(context, eventList -> {
            int i = 0;
            while (i < eventList.size() - 1 && eventList.get(i).getCapacity() < eventList.get(i + 1).getCapacity()) {
                if (eventList.get(i).getCapacity() < eventList.get(i + 1).getCapacity()) {
                    Event temp = eventList.get(i);
                    eventList.set(i, eventList.get(i+1));
                    eventList.set(i+1, temp);
                }
                i++;
            }
//            selectItem(eventList);
            //mutate the list
            //notifychange
        });

    }
}



