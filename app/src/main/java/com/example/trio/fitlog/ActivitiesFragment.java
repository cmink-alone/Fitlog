package com.example.trio.fitlog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.trio.fitlog.adapter.ActivityAdapter;
import com.example.trio.fitlog.database.SqliteDbHelper;
import com.example.trio.fitlog.model.Activity;

import java.util.List;


public class ActivitiesFragment extends Fragment {
    private FloatingActionButton fab;
    private Toolbar toolbar_detail;
    private RecyclerView activity_list;
    public ActivityAdapter adapter;
    public List<Activity> activityList;
    public ActivitiesFragment() {}

    public static ActivitiesFragment newInstance() {
        ActivitiesFragment fragment = new ActivitiesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        activityList = SqliteDbHelper.getInstance(getActivity()).getAllActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_activities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar_detail = getActivity().findViewById(R.id.toolbar_detail);
        getActivity().setTitle("Activities");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar_detail);

        activity_list = getActivity().findViewById(R.id.activity_list);
        fab = getActivity().findViewById(R.id.fab_add_activity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });
        adapter = new ActivityAdapter(getActivity(), activityList);
        activity_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        activity_list.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        activityList = SqliteDbHelper.getInstance(getActivity()).getAllActivity();
        adapter.setItems(activityList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sync:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
