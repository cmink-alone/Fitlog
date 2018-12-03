package com.example.trio.fitlog.model;

import android.provider.BaseColumns;

public class FollowContract implements BaseColumns {
    public static final String TABLE_NAME = "follows";
    public static final String COLUMN_FOLLOWER = "follower_id";
    public static final String COLUMN_FOLLOWING = "following_id";
    public static final String COLUMN_DATE = "icon";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FollowContract.TABLE_NAME + " (" +
                    FollowContract._ID + " INTEGER PRIMARY KEY," +
                    FollowContract.COLUMN_FOLLOWER + " INTEGER," +
                    FollowContract.COLUMN_FOLLOWING + " INTEGER," +
                    FollowContract.COLUMN_DATE + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FollowContract.TABLE_NAME;
}
