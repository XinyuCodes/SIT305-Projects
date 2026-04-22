package com.example.task51c;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/// defining database for bookmark database
@Database(entities = {BookmarkEntity.class}, version = 2, exportSchema = false)
public abstract class BookmarkDatabase extends RoomDatabase {
    private static BookmarkDatabase instance;

    public abstract BookmarkDao bookmarkDao();

    public static synchronized BookmarkDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    BookmarkDatabase.class,
                    "bookmark_database"
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}