package com.example.eventplanner;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class Event {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String category;
    public String dateTime;
    public String location;


    public Event(String title, String category, String dateTime, String location) {
        this.title = title;
        this.category = category;
        this.dateTime = dateTime;
        this.location = location;

    }
}