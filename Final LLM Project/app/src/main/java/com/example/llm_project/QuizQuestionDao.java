package com.example.llm_project;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuizQuestionDao {

    @Insert
    void insert(QuizQuestion question);

    @Query("SELECT * FROM quiz_questions WHERE userId = :userId")
    List<QuizQuestion> getUserTestHistory(int userId);

    @Query("SELECT * FROM quiz_questions WHERE userId = :userId AND topic = :topic AND date in (select max(date) from quiz_questions)")
    List<QuizQuestion> getTopicTestHistory(int userId, String topic);
    //added date limitation post demo so that it just shows the last day's worth of quizzes

    @Query("SELECT * FROM quiz_questions WHERE userId = :userId AND topic = :topic ORDER BY date DESC LIMIT 1")
    List<QuizQuestion> getLatestTopicTestHistory(int userId, String topic);

}
