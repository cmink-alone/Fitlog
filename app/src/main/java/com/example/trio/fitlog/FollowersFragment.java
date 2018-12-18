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
import android.widget.TextView;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowersFragment extends Fragment {
    ProgressBar progressBar;
    private RecyclerView listFollow;
    private List<Profile> followersList = new ArrayList<>();

    FollowAdapter adapter;
    private ApiService apiService;

    public void loadFollowers(){
        Observable<List<Profile>> getFollowers = apiService.getUserFollowers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        getFollowers.subscribe(
                new Observer<List<Profile>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(List<Profile> follows) {
                        followersList = follows;
                        adapter.setFollows(followersList);
                        adapter.notifyDataSetChanged();
                        //Toast.makeText(getContext(), "Loaded followers success", Toast.LENGTH_SHORT).show();
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


    public static FollowersFragment newInstance() {
        FollowersFragment fragment = new FollowersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FollowersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
        adapter = new FollowAdapter(getContext(), followersList,2);

        listFollow.setAdapter(adapter);
        listFollow.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        loadFollowers();
    }
}
