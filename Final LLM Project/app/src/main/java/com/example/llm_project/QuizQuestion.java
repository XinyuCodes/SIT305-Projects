package com.example.llm_project;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "quiz_questions")
public class QuizQuestion {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public String topic;
    public long date;
    public String question;
    public String answer;
    public String correctAnswer;
    public int score;
}
