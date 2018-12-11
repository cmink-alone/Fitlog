package com.example.trio.fitlog.api;

import android.content.Context;

import com.example.trio.fitlog.utils.Constant;
import com.example.trio.fitlog.utils.PreferencesHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static ApiService getService(Context context){
        final PreferencesHelper preferencesHelper = new PreferencesHelper(context);

        OkHttpClient client=new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request;
                        if (preferencesHelper.getLogin()){
                            request=chain
                                    .request()
                                    .newBuilder()
                                    .addHeader("Content-Type","application/json")
                                    .addHeader("Authorization","Bearer "+preferencesHelper.getToken())
                                    .build();
                        }else {
                            request=chain
                                    .request()
                                    .newBuilder()
                                    .addHeader("Content-Type","application/json")
                                    .build();
                        }
                        return chain.proceed(request);
                    }
                }).build();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Constant.URL.api())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit.create(ApiService.class);
    }
}
