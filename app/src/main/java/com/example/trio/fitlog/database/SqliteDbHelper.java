package com.example.trio.fitlog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.ArrayMap;

import com.example.trio.fitlog.R;
import com.example.trio.fitlog.model.Activity;
import com.example.trio.fitlog.model.ActivityContract;
import com.example.trio.fitlog.model.Profile;
import com.example.trio.fitlog.model.ProfileContract;
import com.example.trio.fitlog.model.Type;
import com.example.trio.fitlog.model.TypeContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SqliteDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 21;
    public static final String DATABASE_NAME = "Fitlog.db";
    public static SqliteDbHelper instance;

    private SqliteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SqliteDbHelper getInstance(Context context){
        if (instance == null){
            return new SqliteDbHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ActivityContract.SQL_CREATE_ENTRIES);
        db.execSQL(TypeContract.SQL_CREATE_ENTRIES);
        db.execSQL(ProfileContract.SQL_CREATE_ENTRIES);
        insertTypes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ActivityContract.SQL_DELETE_ENTRIES);
        db.execSQL(TypeContract.SQL_DELETE_ENTRIES);
        db.execSQL(ProfileContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    //ACTIVITY
    public List<Activity> getAllActivity(){
        List<Activity> activities = new ArrayList<>();
        String qry = "SELECT * FROM " + ActivityContract.TABLE_NAME + " ORDER BY " + ActivityContract.COLUMN_DATETIME + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(qry, null);
        if (cursor.moveToFirst()) {
            do {
                Activity activity = new Activity();

                activity.setId(cursor.getInt(cursor.getColumnIndex(ActivityContract._ID)));
                activity.setTitle(cursor.getString(cursor.getColumnIndex(ActivityContract.COLUMN_TITLE)));
                activity.setUser_id(cursor.getInt(cursor.getColumnIndex(ActivityContract.COLUMN_USER_ID)));
                activity.setType_id(cursor.getInt(cursor.getColumnIndex(ActivityContract.COLUMN_TYPE_ID)));
                activity.setDistance(cursor.getInt(cursor.getColumnIndex(ActivityContract.COLUMN_DISTANCE)));
                activity.setHour(cursor.getInt(cursor.getColumnIndex(ActivityContract.COLUMN_HOUR)));
                activity.setMinute(cursor.getInt(cursor.getColumnIndex(ActivityContract.COLUMN_MINUTE)));
                activity.setDatetime(cursor.getString(cursor.getColumnIndex(ActivityContract.COLUMN_DATETIME)));

                Type type = getType(activity.getType_id());

                activity.setMet(type.getMet());
                activity.setStep(type.getStep());

                activities.add(activity);
            } while (cursor.moveToNext());
        }
        db.close();
        return activities;
    }


    public ArrayMap<String, Integer> getActivitySummary(String date){
        List<Activity> activities = new ArrayList<>();
        String qry = "SELECT SUM("+ActivityContract.COLUMN_DISTANCE+") as sum_distance," +
                "SUM("+ActivityContract.COLUMN_HOUR+ "*60+" + ActivityContract.COLUMN_MINUTE +") as sum_minute " +
                "FROM " + ActivityContract.TABLE_NAME + " WHERE date(" + ActivityContract.COLUMN_DATETIME + ")='" + date + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;

        ArrayMap<String, Integer> summary = new ArrayMap<String, Integer>();
        summary.put("sum_distance", cursor.getInt(cursor.getColumnIndex("sum_distance")));
        summary.put("sum_minute", cursor.getInt(cursor.getColumnIndex("sum_minute")));
        db.close();

        return summary;
    }

    public long insertActivity(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ActivityContract.COLUMN_TITLE, activity.getTitle());
        values.put(ActivityContract.COLUMN_USER_ID, activity.getUser_id());
        values.put(ActivityContract.COLUMN_TYPE_ID, activity.getType_id());
        values.put(ActivityContract.COLUMN_DISTANCE, activity.getDistance());
        values.put(ActivityContract.COLUMN_HOUR, activity.getHour());
        values.put(ActivityContract.COLUMN_MINUTE, activity.getMinute());
        values.put(ActivityContract.COLUMN_DATETIME, activity.getDatetime());
        values.put(ActivityContract.COLUMN_DESCRIPTION, activity.getDescription());

        long id = db.insert(ActivityContract.TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public long insertActivityId(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ActivityContract._ID, activity.getId());
        values.put(ActivityContract.COLUMN_TITLE, activity.getTitle());
        values.put(ActivityContract.COLUMN_USER_ID, activity.getUser_id());
        values.put(ActivityContract.COLUMN_TYPE_ID, activity.getType_id());
        values.put(ActivityContract.COLUMN_DISTANCE, activity.getDistance());
        values.put(ActivityContract.COLUMN_HOUR, activity.getHour());
        values.put(ActivityContract.COLUMN_MINUTE, activity.getMinute());
        values.put(ActivityContract.COLUMN_DATETIME, activity.getDatetime());
        values.put(ActivityContract.COLUMN_DESCRIPTION, activity.getDescription());

        long id = db.insert(ActivityContract.TABLE_NAME, null, values);

        db.close();

        return id;
    }
    public void insertActivities(List<Activity> activities) {
        emptyActivity();
        for (Activity activity: activities) {
            insertActivityId(activity);
        }
    }

    public int editActivity(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ActivityContract.COLUMN_TITLE, activity.getTitle());
        values.put(ActivityContract.COLUMN_TYPE_ID, activity.getType_id());
        values.put(ActivityContract.COLUMN_DISTANCE, activity.getDistance());
        values.put(ActivityContract.COLUMN_HOUR, activity.getHour());
        values.put(ActivityContract.COLUMN_MINUTE, activity.getMinute());
        values.put(ActivityContract.COLUMN_DATETIME, activity.getDatetime());
        values.put(ActivityContract.COLUMN_DESCRIPTION, activity.getDescription());

        return db.update(ActivityContract.TABLE_NAME,  values,ActivityContract._ID + " = ?",
                new String[]{String.valueOf(activity.getId())});
    }


    public void emptyActivity(){
        SQLiteDatabase db = this.getWritableDatabase();
        String qry = "DELETE FROM " + ActivityContract.TABLE_NAME;
        db.execSQL(qry);
    }

    public void deleteActivity(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ActivityContract.TABLE_NAME,ActivityContract._ID + " = ?",
                new String[]{String.valueOf(activity.getId())});
    }

    //PROFILE
    public long insertProfile(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProfileContract.COLUMN_NAME, profile.getName());
        values.put(ProfileContract.COLUMN_MOVE_MINUTES, profile.getMove_minutes());
        values.put(ProfileContract.COLUMN_MOVE_DISTANCE, profile.getMove_distance());
        values.put(ProfileContract.COLUMN_GENDER, profile.getGender());
        values.put(ProfileContract.COLUMN_BIRTHDAY, profile.getBirthday());
        values.put(ProfileContract.COLUMN_WEIGHT, profile.getWeight());
        values.put(ProfileContract.COLUMN_HEIGHT, profile.getHeight());

        long id = db.insert(ProfileContract.TABLE_NAME, null, values);
        return id;
    }

    //PROFILE
    public long insertProfileId(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProfileContract._ID, profile.getId());
        values.put(ProfileContract.COLUMN_NAME, profile.getName());
        values.put(ProfileContract.COLUMN_MOVE_MINUTES, profile.getMove_minutes());
        values.put(ProfileContract.COLUMN_MOVE_DISTANCE, profile.getMove_distance());
        values.put(ProfileContract.COLUMN_GENDER, profile.getGender());
        values.put(ProfileContract.COLUMN_BIRTHDAY, profile.getBirthday());
        values.put(ProfileContract.COLUMN_WEIGHT, profile.getWeight());
        values.put(ProfileContract.COLUMN_HEIGHT, profile.getHeight());

        long id = db.insert(ProfileContract.TABLE_NAME, null, values);
        return id;
    }

    public void insertProfiles(List<Profile> profiles) {
        emptyProfiles();
        for (Profile profile: profiles) {
            insertProfileId(profile);
        }
    }

    public void emptyProfiles(){
        SQLiteDatabase db = this.getWritableDatabase();
        String qry = "DELETE FROM " + ProfileContract.TABLE_NAME;
        db.execSQL(qry);
    }

    public int editProfile(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProfileContract.COLUMN_NAME, profile.getName());
        values.put(ProfileContract.COLUMN_MOVE_MINUTES, profile.getMove_minutes());
        values.put(ProfileContract.COLUMN_MOVE_DISTANCE, profile.getMove_distance());
        values.put(ProfileContract.COLUMN_GENDER, profile.getGender());
        values.put(ProfileContract.COLUMN_BIRTHDAY, profile.getBirthday());
        values.put(ProfileContract.COLUMN_WEIGHT, profile.getWeight());
        values.put(ProfileContract.COLUMN_HEIGHT, profile.getHeight());

        return db.update(ProfileContract.TABLE_NAME,  values,ProfileContract._ID + " = ?",
                new String[]{String.valueOf(profile.getId())});
    }

    public Profile getProfile(int id){
        String qry = "SELECT * FROM " + ProfileContract.TABLE_NAME + " WHERE " + ProfileContract._ID + "="+ id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;

        Profile profile = new Profile(
                cursor.getInt(cursor.getColumnIndex(ProfileContract._ID)),
                cursor.getString(cursor.getColumnIndex(ProfileContract.COLUMN_NAME)),
                cursor.getInt(cursor.getColumnIndex(ProfileContract.COLUMN_MOVE_MINUTES)),
                cursor.getInt(cursor.getColumnIndex(ProfileContract.COLUMN_MOVE_DISTANCE)),
                cursor.getString(cursor.getColumnIndex(ProfileContract.COLUMN_GENDER)),
                cursor.getString(cursor.getColumnIndex(ProfileContract.COLUMN_BIRTHDAY)),
                cursor.getInt(cursor.getColumnIndex(ProfileContract.COLUMN_WEIGHT)),
                cursor.getInt(cursor.getColumnIndex(ProfileContract.COLUMN_HEIGHT))
        );
        db.close();
        return profile;
    }

    //TYPE
    public List<Type> getAllType(){
        List<Type> types = new ArrayList<>();
        String qry = "SELECT * FROM " + TypeContract.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(qry, null);
        if (cursor.moveToFirst()) {
            do {
                Type type = new Type(
                        cursor.getInt(cursor.getColumnIndex(TypeContract._ID)),
                        cursor.getString(cursor.getColumnIndex(TypeContract.COLUMN_NAME)),
                        cursor.getInt(cursor.getColumnIndex(TypeContract.COLUMN_ICON)),
                        cursor.getInt(cursor.getColumnIndex(TypeContract.COLUMN_MET)),
                        cursor.getInt(cursor.getColumnIndex(TypeContract.COLUMN_STEP))
                );
                types.add(type);
            } while (cursor.moveToNext());
        }
        db.close();
        return types;
    }

    public Type getType(int id){
        String qry = "SELECT * FROM " + TypeContract.TABLE_NAME + " WHERE " + TypeContract._ID + "="+ id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;

        Type type = new Type(
                cursor.getInt(cursor.getColumnIndex(TypeContract._ID)),
                cursor.getString(cursor.getColumnIndex(TypeContract.COLUMN_NAME)),
                cursor.getInt(cursor.getColumnIndex(TypeContract.COLUMN_ICON)),
                cursor.getInt(cursor.getColumnIndex(TypeContract.COLUMN_MET)),
                cursor.getInt(cursor.getColumnIndex(TypeContract.COLUMN_STEP))
        );
        db.close();
        return type;
    }

    public void insertTypes(SQLiteDatabase db){
        List<String> type_names = Arrays.asList("Running","Cycling","Swimming");
        List<Integer> type_icons = Arrays.asList(R.drawable.running_man,R.drawable.cycling_man,R.drawable.swimming_man);
        List<Integer> type_mets = Arrays.asList(6,3,6);
        List<Integer> type_is_steps = Arrays.asList(1,0,0);

        for (int i = 0; i < type_names.size(); i++){
            ContentValues values = new ContentValues();
            values.put(TypeContract.COLUMN_NAME, type_names.get(i));
            values.put(TypeContract.COLUMN_ICON, type_icons.get(i));
            values.put(TypeContract.COLUMN_MET, type_mets.get(i));
            values.put(TypeContract.COLUMN_STEP, type_is_steps.get(i));
            db.insert(TypeContract.TABLE_NAME, null, values);
        }
    }

}
