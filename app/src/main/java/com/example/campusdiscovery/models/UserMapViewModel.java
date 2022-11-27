package com.example.campusdiscovery.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserMapViewModel extends ViewModel {
    private final MutableLiveData<Map<UUID, Attendee>> selectedItem = new MutableLiveData<Map<UUID, Attendee>>();

    public void selectItem(Map<UUID, Attendee> item) {
        selectedItem.setValue(item);
    }
    public LiveData<Map<UUID, Attendee>> getSelectedItem() {
        return selectedItem;
    }
}
