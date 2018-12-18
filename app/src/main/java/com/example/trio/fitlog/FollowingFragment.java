package com.example.trio.fitlog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.trio.fitlog.adapter.FollowAdapter;
import com.example.trio.fitlog.api.ApiClient;
import com.example.trio.fitlog.api.ApiService;
import com.example.trio.fitlog.model.Profile;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class FollowingFragment extends Fragment {
    ProgressBar progressBar;
    private RecyclerView listFollow;
    private List<Profile> followingList = new ArrayList<>();

    FollowAdapter adapter;
    private ApiService apiService;

    public void loadFollowing(){
        Observable<List<Profile>> getFollowing = apiService.getUserFollowing()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        getFollowing.subscribe(
                new Observer<List<Profile>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(List<Profile> follows) {
                        followingList = follows;
                        adapter.setFollows(followingList);
                        adapter.notifyDataSetChanged();
                        //Toast.makeText(getContext(), "Loaded following success", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onComplete() {

                    }
                }
        );
    }

    public FollowingFragment() {
        // Required empty public constructor
    }

    public static FollowingFragment newInstance() {
        FollowingFragment fragment = new FollowingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ApiClient.getService(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_follow_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progress_circular);
        listFollow = view.findViewById(R.id.list_follow);
        adapter = new FollowAdapter(getContext(), followingList, 1);
        listFollow.setAdapter(adapter);
        listFollow.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        loadFollowing();
    }
}
