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

    @Query("SELECT * FROM quiz_sessions where userId = :userId and date >= :sevenDaysAgo")
    List<QuizSession> getRecentSessions(int userId, long sevenDaysAgo);

}
