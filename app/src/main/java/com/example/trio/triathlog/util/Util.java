package com.example.trio.triathlog.util;

import com.example.trio.triathlog.model.Activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {
    public static String calendarToStringFriendly(Calendar calendar, String pattern){
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Calendar now = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        if(df.format(calendar.getTime()).equals(df.format(now.getTime())))
            return "Today";
        else if(df.format(calendar.getTime()).equals(df.format(yesterday.getTime())))
            return "Yesterday";
        return df.format(calendar.getTime());
    }
    public static String calendarToString(Calendar calendar, String pattern){
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(calendar.getTime());
    }
    public static Calendar stringToCalendar(String date, String pattern){
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return calendar;
        }
    }
    public static Date stringToDate(String str, String pattern){
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return date;
        }
    }

    public static int heartPoint(int minute, Activity activity){
        return minute * 2;
    }

    public static long kmToCalories(int km){
        return (long) (km * 1312.336);
    }
}
