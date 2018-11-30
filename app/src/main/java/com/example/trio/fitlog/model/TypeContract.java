package com.example.trio.fitlog.model;

import android.provider.BaseColumns;

public class TypeContract implements BaseColumns {
    public static final String TABLE_NAME = "types";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_MET = "met";
    public static final String COLUMN_STEP = "is_step";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TypeContract.TABLE_NAME + " (" +
                    TypeContract._ID + " INTEGER PRIMARY KEY," +
                    TypeContract.COLUMN_NAME + " TEXT," +
                    TypeContract.COLUMN_ICON + " TEXT," +
                    TypeContract.COLUMN_MET + " INT," +
                    TypeContract.COLUMN_STEP + " INT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TypeContract.TABLE_NAME;

}