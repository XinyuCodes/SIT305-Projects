package com.example.task51c;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

/// DAO for bookmark
@Dao
public interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BookmarkEntity bookmark);

    @Delete
    void delete(BookmarkEntity bookmark);

    @Query("SELECT * FROM bookmarks")
    LiveData<List<BookmarkEntity>> getAllBookmarks();

    @Query("SELECT * FROM bookmarks WHERE id = :id")
    BookmarkEntity getBookmarkById(int id);
}