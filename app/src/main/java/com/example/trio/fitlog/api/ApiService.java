package com.example.trio.fitlog.api;


import com.example.trio.fitlog.model.Activity;
import com.example.trio.fitlog.model.Auth;
import com.example.trio.fitlog.model.Profile;

import java.util.List;

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

    @GET("activity/following")
    Call<List<Activity>> getAllActivity();
}
