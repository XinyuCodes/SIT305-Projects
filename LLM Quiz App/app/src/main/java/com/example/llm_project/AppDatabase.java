package com.example.llm_project;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, UserInterest.class, QuizSession.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    //setting up abstract methods

    public abstract UserDao userDAO();
    public abstract QuizSessionDao quizSessionDao();
    public abstract UserInterestDao userInterestDao();

    public static synchronized AppDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "QuizAppDatabase").fallbackToDestructiveMigration().build();
        }
        return instance;
    }

}
