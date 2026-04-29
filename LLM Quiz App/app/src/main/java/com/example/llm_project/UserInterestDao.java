package com.example.llm_project;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserInterestDao {

    @Insert
    void insert(UserInterest interest);

    @Query("SELECT * FROM user_interests WHERE userId = :userId")
    List<UserInterest> getInterestsForUser(int userId);

    @Query("SELECT DISTINCT topic FROM user_interests WHERE userId = :userId")
    List<String> getDistinctTopicsForUser(int userId);
}
