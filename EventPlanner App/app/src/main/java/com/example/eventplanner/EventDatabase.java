package com.example.eventplanner;

import android.app.Application;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Event.class}, version = 1, exportSchema = false)
public abstract class EventDatabase extends RoomDatabase{

    private static EventDatabase instance;
    public abstract EventDao eventDao();
    public static EventDatabase getInstance(Application application) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    application.getApplicationContext(),
                    EventDatabase.class,
                    "event_database"
            ).build();
        }
        return instance;


//
//    public abstract EventDao eventDao();
//    private static volatile EventDatabase INSTANCE;
//    public static EventDatabase getDatabase(Context context) {
//        if (INSTANCE == null) {
//            synchronized (EventDatabase.class) {
//                INSTANCE = Room.databaseBuilder(
//                        context.getApplicationContext(),
//                        EventDatabase.class,
//                        "event_database"
//                ).build();
//            }
//        }
//        return INSTANCE;
//    }

    }
}
