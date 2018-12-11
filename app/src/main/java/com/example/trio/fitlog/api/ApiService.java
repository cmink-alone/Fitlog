package com.example.trio.fitlog.api;


import com.example.trio.fitlog.model.Activity;
import com.example.trio.fitlog.model.ApiResponse;
import com.example.trio.fitlog.model.Auth;
import com.example.trio.fitlog.model.Profile;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("login")
    Call<Auth> login(@Field("username") String username, @Field("password") String password);

    @GET("user/all")
    Call<List<Profile>> getAllUser();

    @FormUrlEncoded
    @POST("register")
    Call<Auth> register(
            @Field("username") String username,
            @Field("password") String password,
            @Field("name") String name,
            @Field("birthday") String birthday,
            @Field("gender") String gender
    );

    @GET("activity/following")
    Observable<List<Activity>> getAllActivity();

    @FormUrlEncoded
    @POST("activity/add")
    Call<ApiResponse> addActivity(
            @Field("id") int id,
            @Field("user_id") int user_id,
            @Field("title") String title,
            @Field("datetime") String datetime,
            @Field("type_id") int type_id,
            @Field("hour") int hour,
            @Field("minute") int minute,
            @Field("distance") int distance,
            @Field("description") String description
    );

    @FormUrlEncoded
    @POST("activity/add/sync")
    Observable<List<Activity>> addSyncActivity(
            @Field("activities") String activities
    );

    @FormUrlEncoded
    @POST("activity/update")
    Call<ApiResponse> updateActivity(
            @Field("id") int id,
            @Field("user_id") int user_id,
            @Field("title") String title,
            @Field("datetime") String datetime,
            @Field("type_id") int type_id,
            @Field("hour") int hour,
            @Field("minute") int minute,
            @Field("distance") int distance,
            @Field("description") String description,
            @Field("flag_delete") int flag_delete
    );


    @FormUrlEncoded
    @POST("activity/update/sync")
    Observable<ApiResponse> updateSyncActivity(
            @Field("activities") String activities
    );
}
