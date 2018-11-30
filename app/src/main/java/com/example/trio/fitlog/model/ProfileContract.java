package com.example.trio.fitlog.model;

import android.provider.BaseColumns;

public class ProfileContract implements BaseColumns {
    public static final String TABLE_NAME = "profile";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MOVE_MINUTES = "move_minutes";
    public static final String COLUMN_MOVE_DISTANCE = "move_distance";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_HEIGHT = "height";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ProfileContract.TABLE_NAME + " (" +
                    ProfileContract._ID + " INTEGER PRIMARY KEY," +
                    ProfileContract.COLUMN_NAME + " TEXT," +
                    ProfileContract.COLUMN_MOVE_MINUTES + " INTEGER," +
                    ProfileContract.COLUMN_MOVE_DISTANCE + " INTEGER," +
                    ProfileContract.COLUMN_GENDER + " TEXT," +
                    ProfileContract.COLUMN_BIRTHDAY + " TEXT," +
                    ProfileContract.COLUMN_WEIGHT + " INTEGER," +
                    ProfileContract.COLUMN_HEIGHT + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ProfileContract.TABLE_NAME;
}
