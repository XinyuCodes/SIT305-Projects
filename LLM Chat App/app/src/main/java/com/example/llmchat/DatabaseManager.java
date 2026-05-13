package com.example.llmchat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void open(){
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public void insertMessage(String email, String message, String speaker){
        String sql = "INSERT INTO " + DatabaseHelper.TABLE_NAME + " (" + 
                DatabaseHelper.COLUMN_EMAIL + ", " + 
                DatabaseHelper.COLUMN_MESSAGE + ", " + 
                DatabaseHelper.COLUMN_SPEAKER + ") VALUES (?, ?, ?)";
        db.execSQL(sql, new String[]{email, message, speaker});
    }

    public List<Message> getMessagesByEmail(String email) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.COLUMN_EMAIL + " = ? ORDER BY " + DatabaseHelper.COLUMN_ID + " ASC";
        Cursor cursor = db.rawQuery(sql, new String[]{email});
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String msgEmail = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL));
            String msgText = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MESSAGE));
            String speaker = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SPEAKER));
            String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIMESTAMP));
            
            messages.add(new Message(id, msgEmail, msgText, speaker, timestamp));
        }
        cursor.close();
        return messages;
    }
}
