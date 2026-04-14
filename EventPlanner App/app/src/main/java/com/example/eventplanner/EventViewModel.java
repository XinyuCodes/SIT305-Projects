package com.example.eventplanner;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.Executors;

public class EventViewModel extends AndroidViewModel {
    private EventDao eventDao;
    public final LiveData<List<Event>> allEvents;

    public EventViewModel(Application application) {
        super(application);
        eventDao = EventDatabase.getInstance(application).eventDao();
        allEvents = eventDao.getAllEvents();
    }
    public void insert(Event event){
        Executors.newSingleThreadExecutor().execute(()->eventDao.insert(event));
    }

    public void update(Event event){
        Executors.newSingleThreadExecutor().execute(()->eventDao.update(event));
    }

    public void delete(Event event){
        Executors.newSingleThreadExecutor().execute(()->eventDao.delete(event));
    }

}