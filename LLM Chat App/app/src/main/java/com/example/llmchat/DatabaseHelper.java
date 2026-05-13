package com.example.llmchat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chat_database";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "CHAT_MESSAGES";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_SPEAKER = "speaker";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // Constructor - creates a new database
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Call when database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_MESSAGE + " TEXT, " +
                COLUMN_SPEAKER + " TEXT, " +
                COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTable);
    }

    // Call when database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // SQLite does not support ADD COLUMN with CURRENT_TIMESTAMP default.
            // We add the column first, then optionally populate it.
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_TIMESTAMP + " DATETIME");
            db.execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_TIMESTAMP + " = CURRENT_TIMESTAMP WHERE " + COLUMN_TIMESTAMP + " IS NULL");
        }
    }
}
