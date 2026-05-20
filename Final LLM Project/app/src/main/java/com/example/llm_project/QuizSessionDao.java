package com.example.llm_project;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuizSessionDao {

    @Insert
    void insert(QuizSession session);

    @Query("SELECT * FROM quiz_sessions WHERE userId = :userId")
    List<QuizSession> getSessionForuser(int userId);

    @Query("SELECT * FROM quiz_sessions WHERE userId = :userId ORDER BY date DESC LIMIT 1")
    QuizSession getLatestSession(int userId);

}
