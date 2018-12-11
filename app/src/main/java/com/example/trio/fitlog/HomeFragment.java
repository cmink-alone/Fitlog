package com.example.trio.fitlog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trio.fitlog.database.SqliteDbHelper;
import com.example.trio.fitlog.model.Profile;
import com.example.trio.fitlog.utils.PreferencesHelper;
import com.example.trio.fitlog.utils.Util;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;


public class HomeFragment extends Fragment {
    private CollapsingToolbarLayout toolbar_layout;
    SqliteDbHelper sqliteDbHelper;
    private ArrayMap<String, Integer> activitySummary;
    private Calendar homeCalendar = Calendar.getInstance();
    private PreferencesHelper preferencesHelper;
    private Profile profile;

    private TextView move_minute;
    private TextView goal_move_minute;
    private TextView move_distance;
    private TextView goal_move_distance;
    private TextView activities;

    public HomeFragment() {
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        move_minute = view.findViewById(R.id.move_minute);
        goal_move_minute = view.findViewById(R.id.goal_move_minute);
        move_distance = view.findViewById(R.id.move_distance);
        goal_move_distance = view.findViewById(R.id.goal_move_distance);
        activities = view.findViewById(R.id.activities);

        sqliteDbHelper = SqliteDbHelper.getInstance(getContext());
        preferencesHelper = new PreferencesHelper(getContext());
        toolbar_layout = view.findViewById(R.id.toolbar_layout);
        toolbar_layout.setTitle("Today");

        activitySummary = sqliteDbHelper.getActivitySummary(Util.calendarToString(homeCalendar,"yyyy-MM-dd"));
        Profile profile = sqliteDbHelper.getProfile(preferencesHelper.getUserId());

        move_minute.setText(String.valueOf(activitySummary.get("sum_minute")));
        move_distance.setText(String.valueOf(activitySummary.get("sum_distance")));
        goal_move_minute.setText(String.valueOf(profile.getMove_minutes()));
        goal_move_distance.setText(String.valueOf(profile.getMove_distance()));
        activities.setText(String.valueOf(activitySummary.get("count_activity")));
    }
}
