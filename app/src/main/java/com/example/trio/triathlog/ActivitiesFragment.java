package com.example.trio.triathlog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trio.triathlog.adapter.ActivityAdapter;
import com.example.trio.triathlog.database.TriathlogDbHelper;
import com.example.trio.triathlog.model.Activity;

import java.util.List;


public class ActivitiesFragment extends Fragment {
    private FloatingActionButton fab;
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
        activityList = TriathlogDbHelper.getInstance(getActivity()).getAllActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity_list = getActivity().findViewById(R.id.activity_list);
        fab = getActivity().findViewById(R.id.fab_add_activity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });
        getActivity().setTitle("Activities");
        adapter = new ActivityAdapter(getActivity(), activityList);
        activity_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        activity_list.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        activityList = TriathlogDbHelper.getInstance(getActivity()).getAllActivity();
        adapter.setItems(activityList);
        adapter.notifyDataSetChanged();
    }
}
