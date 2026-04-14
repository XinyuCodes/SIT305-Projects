package com.example.eventplanner;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface EventDao{
    //create
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Event event);

    //read
    @Query("SELECT * FROM events ORDER BY dateTime ASC")
    LiveData<List<Event>> getAllEvents();

    //READ - single event by ID
    @Query("SELECT * FROM events WHERE id = :id")
    Event getEventById(int id);

    //update
    @Update
    void update(Event event);
    //Delete
    @Delete
    void delete(Event event);

}
