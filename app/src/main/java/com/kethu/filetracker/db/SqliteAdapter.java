package com.kethu.filetracker.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kethu.filetracker.helpers.Utility;
import com.kethu.filetracker.user.User;

import java.util.ArrayList;
import java.util.List;

import static com.kethu.filetracker.db.MySqiliteHelper.KEY_AADHAR;
import static com.kethu.filetracker.db.MySqiliteHelper.KEY_ADDRESS;
import static com.kethu.filetracker.db.MySqiliteHelper.KEY_ID;
import static com.kethu.filetracker.db.MySqiliteHelper.KEY_MOBILE;
import static com.kethu.filetracker.db.MySqiliteHelper.KEY_NAME;
import static com.kethu.filetracker.db.MySqiliteHelper.KEY_OFFICE;
import static com.kethu.filetracker.db.MySqiliteHelper.KEY_USER_DESN;
import static com.kethu.filetracker.db.MySqiliteHelper.KEY_USER_Status;
import static com.kethu.filetracker.db.MySqiliteHelper.KEY_USER_TYPE;
import static com.kethu.filetracker.db.MySqiliteHelper.TABLE_USERS;

/**
 * Created by satya on 08-Jan-18.
 */

public class SqliteAdapter {

    MySqiliteHelper mMySqiliteHelper;

    public SqliteAdapter(Context context) {
        mMySqiliteHelper = new MySqiliteHelper(context);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new User
    public long addUser(User user) {
        long id = 0;
        try {
            SQLiteDatabase db = mMySqiliteHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_ID, user.getId());
            values.put(KEY_NAME, user.getName());
            values.put(KEY_USER_TYPE, user.getUserType());
            values.put(KEY_USER_DESN, user.getDesignation());
            values.put(KEY_USER_Status, user.getUserStatus());
            values.put(KEY_MOBILE, user.getMobile());
            values.put(KEY_AADHAR, user.getAadhar());
            values.put(KEY_OFFICE, user.getOffice());
            values.put(KEY_ADDRESS, user.getAdd());

            // Inserting Row
            id = db.insert(TABLE_USERS, null, values);
            db.close(); // Closing database connection
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }


    // Getting single User
    public User getUser(int id) {
        SQLiteDatabase db = mMySqiliteHelper.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID,
                        KEY_NAME, KEY_USER_TYPE, KEY_USER_DESN, KEY_USER_Status}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();


        User user = new User();
        if (cursor != null) {
            user.setId(Integer.parseInt(cursor.getString(0)));
            user.setName(cursor.getString(1));
            user.setUserType(cursor.getString(2));
            user.setDesignation(cursor.getString(3));
            user.setUserStatus(cursor.getString(4));
        }

        return user;
    }

    // Getting All Users
    public List<User> getAllUsers() {

        List<User> users = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = mMySqiliteHelper.getWritableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID,
                        KEY_NAME, KEY_USER_TYPE, KEY_USER_DESN, KEY_USER_Status}, null,
                null, null, null, null, null);

        // looping through all rows and adding to list
        try {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        User user = new User();
                        user.setId(Integer.parseInt(cursor.getString(0)));
                        user.setName(cursor.getString(1));
                        user.setUserType(cursor.getString(2));
                        user.setDesignation(cursor.getString(3));
                        user.setUserStatus(cursor.getString(4));
                        users.add(user);
                    } while (cursor.moveToNext());
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // return User list
        return users;
    }

    // Updating single User
    public int updateUser(User user) {
        SQLiteDatabase db = mMySqiliteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_USER_DESN, user.getDesignation());

        // updating row
        return db.update(TABLE_USERS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
    }

    // Deleting single User
    public void deleteUser(User user) {
        SQLiteDatabase db = mMySqiliteHelper.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID + " = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }


    // Getting users Count
    public int getUserCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = mMySqiliteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
