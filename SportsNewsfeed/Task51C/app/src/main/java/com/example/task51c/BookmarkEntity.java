package com.example.task51c;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/// defining a class for bookmarks & initiate bookmarks
@Entity(tableName = "bookmarks")
public class BookmarkEntity {

    @PrimaryKey
    public int id;
    public String title;
    public String description;
    public int imageResId;
    public String category;

    public BookmarkEntity(int id, String title, String description, int imageResId, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
        this.category = category;
    }
}