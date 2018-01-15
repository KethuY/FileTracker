package com.kethu.filetracker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by satya on 08-Jan-18.
 */

public class MySqiliteHelper extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "user_db";
    public static final String TABLE_USERS = "user";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_USER_TYPE = "user_type";
    public static final String KEY_USER_DESN = "user_desn";
    public static final String KEY_USER_Status = "user_status";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_AADHAR = "aadhar";
    public static final String KEY_OFFICE = "office";
    public static final String KEY_ADDRESS = "address";

    public MySqiliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_USER_TYPE + " TEXT," + KEY_USER_DESN + " TEXT," + KEY_USER_Status + " TEXT,"
                + KEY_MOBILE + " TEXT," + KEY_AADHAR + " TEXT," + KEY_OFFICE + " TEXT," + KEY_ADDRESS + " TEXT )";
        db.execSQL(CREATE_USERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USERS;
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }
}
