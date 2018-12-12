package com.example.trio.fitlog.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.SupportActivity;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.example.trio.fitlog.R;
import com.example.trio.fitlog.model.Activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Util {

    public static boolean profilesLoaded = false;
    public static boolean activitiesLoaded = false;

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

    public static List<Integer> getDatesInMonth(Date date){
        List<Integer> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int month=calendar.get(Calendar.MONTH);

        while (month==calendar.get(Calendar.MONTH)) {
            dates.add(calendar.get(Calendar.DATE));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dates;
    }

    public static void whiteStatusBar(SupportActivity activity){
        Window window = activity.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(activity,R.color.white));
    }

    public static boolean isConnected(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return  activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
