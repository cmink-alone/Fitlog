package com.example.trio.fitlog.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.trio.fitlog.model.Profile;


public class PreferencesHelper {
    private SharedPreferences sharedPreferences;
    private final String PREFERENCES_NAME="shared_preferences";
    private final String LOGIN="login";
    private final String TOKEN="token";
    private final String USER_ID="user_id";

    public PreferencesHelper(Context context) {
        sharedPreferences=context.getSharedPreferences(PREFERENCES_NAME,Context.MODE_PRIVATE);
    }

    public void setLogin(boolean login){
        sharedPreferences.edit().putBoolean(LOGIN,login).apply();
    }

    public boolean getLogin(){
        return sharedPreferences.getBoolean(LOGIN,false);
    }

    public void setToken(String token){
        sharedPreferences.edit().putString(TOKEN,token).apply();
    }

    public String getToken(){
        return sharedPreferences.getString(TOKEN,"");
    }

    public void setUserId(int user_id){
        sharedPreferences.edit()
                .putInt(USER_ID,user_id)
                .apply();
    }

    public int getUserId(){
        return sharedPreferences.getInt(USER_ID,0);
    }

    public void setUserLogin(Profile user, String token){
        setLogin(true);
        setUserId(user.getId());
        setToken(token);
    }

    public void logout(){
        sharedPreferences.edit()
                .clear()
                .apply();
    }
}
