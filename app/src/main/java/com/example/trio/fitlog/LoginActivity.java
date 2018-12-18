package com.example.trio.fitlog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trio.fitlog.api.ApiClient;
import com.example.trio.fitlog.api.ApiService;
import com.example.trio.fitlog.database.SqliteDbHelper;
import com.example.trio.fitlog.model.Activity;
import com.example.trio.fitlog.model.Auth;
import com.example.trio.fitlog.model.Profile;
import com.example.trio.fitlog.utils.PreferencesHelper;
import com.example.trio.fitlog.utils.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    TextView username;
    TextView password;
    TextView register;
    ProgressBar progressBar;

    ApiService apiService;
    PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Util.whiteStatusBar(this);
        preferencesHelper = new PreferencesHelper(this);

        apiService = ApiClient.getService(getApplicationContext());
        if(!preferencesHelper.getToken().equals("")){
            loginSuccess();
        }

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        progressBar = findViewById(R.id.progress_horizontal);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View btnLogin = view;
                btnLogin.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                apiService.login(username.getText().toString(),password.getText().toString())
                        .enqueue(new LoginCallback());
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(register);
            }
        });
    }

    public void loginSuccess(){
        apiService.getAllUser().enqueue(new UserCallback());
        //apiService.getAllActivity().enqueue(new ActivityCallback());

        Intent main = new Intent(LoginActivity.this, MainActivity.class);
        LoginActivity.this.startActivity(main);
        finish();
    }

    class LoginCallback implements Callback<Auth>{
        @Override
        public void onResponse(Call<Auth> call, Response<Auth> response) {
            if(response.isSuccessful()){
                Auth auth = response.body();

                preferencesHelper.setUserLogin(auth.getUser(), auth.getMessage());

                loginSuccess();
                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
            btnLogin.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onFailure(Call<Auth> call, Throwable t) {
            btnLogin.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
        }
    }

    class UserCallback implements Callback<List<Profile>> {
        @Override
        public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
            if(response.isSuccessful()) {
                List<Profile> profiles = response.body();
                SqliteDbHelper.getInstance(getApplicationContext()).insertProfiles(profiles);
                Util.profilesLoaded = true;
            }
        }

        @Override
        public void onFailure(Call<List<Profile>> call, Throwable t) {

        }
    }

    public class ActivityCallback implements Callback<List<Activity>> {
        @Override
        public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
            if(response.isSuccessful()) {
                List<Activity> activities = response.body();
                SqliteDbHelper.getInstance(getApplicationContext()).insertActivities(activities);
                Util.activitiesLoaded = true;
            }
        }

        @Override
        public void onFailure(Call<List<Activity>> call, Throwable t) {

        }
    }
}
