package com.example.llm_project;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "quiz_sessions")
public class QuizSession {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public String topic;
    public long date;
    public int score;

}
