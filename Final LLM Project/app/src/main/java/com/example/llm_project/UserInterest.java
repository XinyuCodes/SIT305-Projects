package com.example.llm_project;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_interests")
public class UserInterest {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public String firstName;
    public String topic;
}
