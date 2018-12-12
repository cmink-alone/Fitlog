package com.example.trio.fitlog;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trio.fitlog.adapter.DateAdapter;
import com.example.trio.fitlog.database.SqliteDbHelper;
import com.example.trio.fitlog.model.Profile;
import com.example.trio.fitlog.utils.PreferencesHelper;
import com.example.trio.fitlog.utils.Util;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment {
    private Toolbar toolbar_detail;
    SqliteDbHelper sqliteDbHelper;
    private ArrayMap<String, Integer> activitySummary;
    private Calendar homeCalendar = Calendar.getInstance();
    private PreferencesHelper preferencesHelper;
    DateAdapter adapter;
    Profile profile;

    private TextView move_minute;
    private TextView goal_move_minute;
    private TextView move_distance;
    private TextView goal_move_distance;
    private TextView activities;
    private RecyclerView list_date;

    DatePickerDialog datePickerDialog;

    public void dateChanged(int date){
        homeCalendar.set(Calendar.DAY_OF_MONTH, date);
        activitySummary = sqliteDbHelper.getActivitySummary(Util.calendarToString(homeCalendar,"yyyy-MM-dd"));
        populateData();
        adapter.notifyDataSetChanged();
    }

    public void populateData(){
        move_minute.setText(String.valueOf(activitySummary.get("sum_minute")));
        move_distance.setText(String.valueOf(activitySummary.get("sum_distance")));
        goal_move_minute.setText(String.valueOf(profile.getMove_minutes()));
        goal_move_distance.setText(String.valueOf(profile.getMove_distance()));
        activities.setText(String.valueOf(activitySummary.get("count_activity")));
    }

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

        List<Integer> dates = Util.getDatesInMonth(homeCalendar.getTime());

        move_minute = view.findViewById(R.id.move_minute);
        goal_move_minute = view.findViewById(R.id.goal_move_minute);
        move_distance = view.findViewById(R.id.move_distance);
        goal_move_distance = view.findViewById(R.id.goal_move_distance);
        activities = view.findViewById(R.id.activities);
        list_date = view.findViewById(R.id.list_date);

        sqliteDbHelper = SqliteDbHelper.getInstance(getContext());
        preferencesHelper = new PreferencesHelper(getContext());
        profile = sqliteDbHelper.getProfile(preferencesHelper.getUserId());

        activitySummary = sqliteDbHelper.getActivitySummary(Util.calendarToString(homeCalendar,"yyyy-MM-dd"));
        populateData();

        adapter = new DateAdapter(getContext(), dates, homeCalendar.get(Calendar.DATE), this);

        list_date.setAdapter(adapter);
        list_date.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        list_date.smoothScrollToPosition(adapter.getItemPosition(homeCalendar.get(Calendar.DATE)));


        datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int currentMonth = homeCalendar.get(Calendar.MONTH);

                        homeCalendar.set(Calendar.YEAR, year);
                        homeCalendar.set(Calendar.MONTH, monthOfYear);
                        homeCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        if(monthOfYear != currentMonth){
                            adapter.setDates(Util.getDatesInMonth(homeCalendar.getTime()));
                        }
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(Util.calendarToString(homeCalendar, "MMMM"));
                        adapter.setItemPosition(homeCalendar.get(Calendar.DATE));
                        adapter.notifyDataSetChanged();
                        list_date.smoothScrollToPosition(adapter.getItemPosition(homeCalendar.get(Calendar.DATE)));

                        activitySummary = sqliteDbHelper.getActivitySummary(Util.calendarToString(homeCalendar,"yyyy-MM-dd"));
                        populateData();
                    }
                },
                homeCalendar.get(Calendar.YEAR),
                homeCalendar.get(Calendar.MONTH),
                homeCalendar.get(Calendar.DAY_OF_MONTH)
        );

        toolbar_detail = view.findViewById(R.id.toolbar_detail);
        toolbar_detail.setNavigationIcon(R.drawable.ic_arrow_down);
        toolbar_detail.setTitle(Util.calendarToString(homeCalendar,"MMMM"));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar_detail);
        toolbar_detail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
    }


}
