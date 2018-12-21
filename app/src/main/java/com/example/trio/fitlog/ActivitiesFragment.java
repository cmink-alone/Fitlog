package com.example.trio.fitlog;

import android.content.Context;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.trio.fitlog.adapter.ActivityAdapter;
import com.example.trio.fitlog.api.ApiClient;
import com.example.trio.fitlog.api.ApiService;
import com.example.trio.fitlog.database.SqliteDbHelper;
import com.example.trio.fitlog.model.Activity;
import com.example.trio.fitlog.model.ApiResponse;
import com.example.trio.fitlog.utils.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v4.content.ContextCompat.getColorStateList;
import static android.support.v4.content.ContextCompat.getSystemService;


public class ActivitiesFragment extends Fragment {
    private FloatingActionButton fab;
    private Toolbar toolbar_detail;
    private RecyclerView activity_list;
    private ProgressBar progressBar;

    public ActivityAdapter adapter;
    public List<Activity> activityList;

    MenuItem menuItem;

    public ActivitiesFragment() {}

    SqliteDbHelper sqliteDbHelper;

    ApiService apiService;

    public static ActivitiesFragment newInstance() {
        ActivitiesFragment fragment = new ActivitiesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        activityList = SqliteDbHelper.getInstance(getActivity()).getAllActivity();
        apiService = ApiClient.getService(getContext());
        sqliteDbHelper = SqliteDbHelper.getInstance(getActivity());
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

        activity_list = view.findViewById(R.id.activity_list);
        fab = view.findViewById(R.id.fab_add_activity);
        progressBar = view.findViewById(R.id.progress_horizontal);

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
        activityList = sqliteDbHelper.getAllActivity();
        adapter.setItems(activityList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_menu, menu);
        LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = (ImageView) layoutInflater.inflate(R.layout.iv_sync, null);
        menuItem = menu.findItem(R.id.sync).setActionView(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.clockwise_animation);
                rotation.setRepeatCount(Animation.INFINITE);
                menuItem.getActionView().startAnimation(rotation);
                ((ImageView)menuItem.getActionView()).setImageTintList(getActivity().getResources().getColorStateList(R.color.colorPrimaryDark));
                if(Util.isConnected(getActivity())) {
                    progressBar.setVisibility(View.VISIBLE);

                    List<Activity> activitiesPushInsert = sqliteDbHelper.getAllActivityPushInsert();
                    final List<Activity> activitiesPushUpdate = sqliteDbHelper.getAllActivityPushUpdate();

                    Log.e("order", new Gson().toJson(activitiesPushInsert));
                    Log.e("order", new Gson().toJson(activitiesPushUpdate));


                    List<Observable<?>> requests = new ArrayList<>();

                    Observable<List<Activity>> addSync = apiService.addSyncActivity(new Gson().toJson(activitiesPushInsert))
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread());
                    addSync.subscribe(
                            new Observer<List<Activity>>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                }

                                @Override
                                public void onNext(List<Activity> activities) {
                                    sqliteDbHelper.editActivities(activities);
                                    Toast.makeText(getActivity(), "Sync insert success", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {
                                }
                            }
                    );

                    Observable<ApiResponse> editSync = apiService.updateSyncActivity(new Gson().toJson(activitiesPushUpdate))
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread());
                    editSync.subscribe(
                            new Observer<ApiResponse>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(ApiResponse apiResponse) {
                                    Toast.makeText(getActivity(), "Sync update success", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {

                                }
                            }
                    );

                    Observable<List<Activity>> selectSync = apiService.getAllActivity()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread());
                    selectSync.subscribe(
                            new Observer<List<Activity>>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(List<Activity> activities) {
                                    SqliteDbHelper.getInstance(getContext()).insertActivities(activities);
                                    Util.activitiesLoaded = true;
                                    activityList = SqliteDbHelper.getInstance(getActivity()).getAllActivity();
                                    adapter.setItems(activityList);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "All activites loaded", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {

                                }
                            }
                    );

                    /*
                    editSync.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getActivity(), "Sync update success", Toast.LENGTH_SHORT).show();
                            } else {

                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                    selectSync.enqueue(new Callback<List<Activity>>() {
                        @Override
                        public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
                            List<Activity> activities = response.body();
                            SqliteDbHelper.getInstance(getContext()).insertActivities(activities);
                            Util.activitiesLoaded = true;
                            activityList = SqliteDbHelper.getInstance(getActivity()).getAllActivity();
                            adapter.setItems(activityList);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "All activites loaded", Toast.LENGTH_SHORT).show();
                            menuItem.getActionView().clearAnimation();
                            ((ImageView)menuItem.getActionView()).setImageTintList(getActivity().getResources().getColorStateList(R.color.black));
                        }

                        @Override
                        public void onFailure(Call<List<Activity>> call, Throwable t) {

                        }
                    });
                    */

                    requests.add(addSync);
                    requests.add(editSync);
                    requests.add(selectSync);

                    Observable.zip(
                            requests,
                            new Function<Object[], Object>() {
                                @Override
                                public Object apply(Object[] objects) throws Exception {
                                    // Objects[] is an array of combined results of completed requests

                                    // do something with those results and emit new event
                                    progressBar.setVisibility(View.INVISIBLE);
                                    menuItem.getActionView().clearAnimation();
                                    ((ImageView)menuItem.getActionView()).setImageTintList(getActivity().getResources().getColorStateList(R.color.black));

                                    Toast.makeText(getContext(), "All Request is completed", Toast.LENGTH_SHORT).show();
                                    return new Object();
                                }
                            })
                            // After all requests had been performed the next observer will receive the Object, returned from Function
                            .subscribe(
                                    // Will be triggered if all requests will end successfully (4xx and 5xx also are successful requests too)
                                    new Consumer<Object>() {
                                        @Override
                                        public void accept(Object o) throws Exception {
                                            //Do something on successful completion of all requests
                                        }
                                    },

                                    // Will be triggered if any error during requests will happen
                                    new Consumer<Throwable>() {
                                        @Override
                                        public void accept(Throwable e) throws Exception {
                                            //Do something on error completion of requests
                                        }
                                    }
                            );

                    /*
                    apiService.addSyncActivity(new Gson().toJson(activitiesPushInsert)).enqueue(
                            new Callback<List<Activity>>() {
                                @Override
                                public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
                                    if (response.isSuccessful()) {
                                        List<Activity> activities = response.body();
                                        sqliteDbHelper.editActivities(activities);
                                        Toast.makeText(getActivity(), "Sync insert success", Toast.LENGTH_SHORT).show();
                                    } else {

                                    }
                                    progressBar.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onFailure(Call<List<Activity>> call, Throwable t) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                    );

                    apiService.updateSyncActivity(new Gson().toJson(activitiesPushUpdate)).enqueue(
                            new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Sync update success", Toast.LENGTH_SHORT).show();
                                        apiService.getAllActivity().enqueue(
                                                new Callback<List<Activity>>() {
                                                    @Override
                                                    public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
                                                        List<Activity> activities = response.body();
                                                        SqliteDbHelper.getInstance(getContext()).insertActivities(activities);
                                                        Util.activitiesLoaded = true;
                                                        activityList = SqliteDbHelper.getInstance(getActivity()).getAllActivity();
                                                        adapter.setItems(activityList);
                                                        adapter.notifyDataSetChanged();
                                                        Toast.makeText(getActivity(), "All activites loaded", Toast.LENGTH_SHORT).show();
                                                        menuItem.getActionView().clearAnimation();
                                                        ((ImageView)menuItem.getActionView()).setImageTintList(getActivity().getResources().getColorStateList(R.color.black));
                                                    }

                                                    @Override
                                                    public void onFailure(Call<List<Activity>> call, Throwable t) {

                                                    }
                                                }
                                        );
                                    } else {

                                    }
                                    progressBar.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable t) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                    );
                    */

                } else {
                    List<Activity> activitiesPushInsert = sqliteDbHelper.getAllActivityPushInsert();
                    final List<Activity> activitiesPushUpdate = sqliteDbHelper.getAllActivityPushUpdate();

                    Log.e("order", new Gson().toJson(activitiesPushInsert));
                    Log.e("order", new Gson().toJson(activitiesPushUpdate));
                    Toast.makeText(getActivity(), "Turn on internet to sync", Toast.LENGTH_SHORT).show();
                }
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}
