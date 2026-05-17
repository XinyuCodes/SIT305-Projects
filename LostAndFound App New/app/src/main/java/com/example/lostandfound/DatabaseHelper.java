package com.example.lostandfound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "lostandfound";
    static final int DATABASE_VERSION = 3;
    static final String TABLE_NAME = "advertsTable";

    static final String COL_ID = "id";
    static final String COL_POST_TYPE = "post_type";
    static final String COL_NAME = "name";
    static final String COL_PHONE = "phone";
    static final String COL_DESCRIPTION = "description";
    static final String COL_DATE = "date";
    static final String COL_LOCATION = "location";
    static final String COL_CATEGORY = "category";
    static final String COL_IMAGE_PATH = "image_path";
    static final String COL_TIMESTAMP = "timestamp";
    static final String COL_LATITUDE = "latitude";
    static final String COL_LONGITUDE = "longitude";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_POST_TYPE + " TEXT,"
                + COL_NAME + " TEXT,"
                + COL_PHONE + " TEXT,"
                + COL_DESCRIPTION + " TEXT,"
                + COL_DATE + " TEXT,"
                + COL_LOCATION + " TEXT,"
                + COL_CATEGORY + " TEXT,"
                + COL_IMAGE_PATH + " TEXT,"
                + COL_TIMESTAMP + " TEXT,"
                + COL_LATITUDE + " REAL,"
                + COL_LONGITUDE + " REAL"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertAdvert(Advert advert) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_POST_TYPE, advert.getPostType());
        values.put(COL_NAME, advert.getName());
        values.put(COL_PHONE, advert.getPhone());
        values.put(COL_DESCRIPTION, advert.getDescription());
        values.put(COL_DATE, advert.getDate());
        values.put(COL_LOCATION, advert.getLocation());
        values.put(COL_CATEGORY, advert.getCategory());
        values.put(COL_IMAGE_PATH, advert.getImagePath());
        values.put(COL_TIMESTAMP, advert.getTimestamp());
        values.put(COL_LATITUDE, advert.getLatitude());
        values.put(COL_LONGITUDE, advert.getLongitude());

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public List<Advert> getAllAdverts() {
        List<Advert> advertList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                advertList.add(createAdvertFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return advertList;
    }

    public Advert getAdvertById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COL_ID + "=?", 
                new String[]{String.valueOf(id)}, null, null, null);

        Advert advert = null;
        if (cursor.moveToFirst()) {
            advert = createAdvertFromCursor(cursor);
        }

        cursor.close();
        db.close();
        return advert;
    }

    private Advert createAdvertFromCursor(Cursor cursor) {
        Advert advert = new Advert();
        advert.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
        advert.setPostType(cursor.getString(cursor.getColumnIndexOrThrow(COL_POST_TYPE)));
        advert.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
        advert.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)));
        advert.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)));
        advert.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)));
        advert.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION)));
        advert.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)));
        advert.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE_PATH)));
        advert.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(COL_TIMESTAMP)));
        advert.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LATITUDE)));
        advert.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LONGITUDE)));
        return advert;
    }

    //
    public void deleteAdvert(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}