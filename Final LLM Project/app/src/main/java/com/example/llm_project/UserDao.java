package com.example.llm_project;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert
    long insert(User user);

    @Query("SELECT * FROM users where userName = :username and passWord = :password")
    User login(String username, String password);

    @Query("SELECT * FROM users where userName = :username")
    User findByUsername(String username);

    @Query("SELECT * FROM users where id = :userId")
    User getUserById(int userId);
}
