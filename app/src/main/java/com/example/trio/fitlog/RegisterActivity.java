package com.example.trio.fitlog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trio.fitlog.api.ApiClient;
import com.example.trio.fitlog.api.ApiService;
import com.example.trio.fitlog.database.SqliteDbHelper;
import com.example.trio.fitlog.model.ApiResponse;
import com.example.trio.fitlog.model.Auth;
import com.example.trio.fitlog.model.Profile;
import com.example.trio.fitlog.utils.PreferencesHelper;
import com.example.trio.fitlog.utils.Util;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private Calendar birthdayCalendar = Calendar.getInstance();

    private TextView name_input;
    private TextView birthday_input;
    private TextView gender_input;
    private TextView username_input;
    private TextView password_input;
    private TextView repassword_input;
    private Toolbar toolbar_detail;
    private ProgressBar progressBar;

    PreferencesHelper preferencesHelper;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        preferencesHelper = new PreferencesHelper(this);
        apiService = ApiClient.getService(this);

        Util.whiteStatusBar(this);

        name_input = findViewById(R.id.name_input);
        birthday_input = findViewById(R.id.birthday_reg);
        gender_input = findViewById(R.id.gender_input);
        username_input = findViewById(R.id.username_input);
        password_input = findViewById(R.id.password_input);
        repassword_input = findViewById(R.id.repassword_input);
        progressBar = findViewById(R.id.progress_horizontal);

        toolbar_detail = findViewById(R.id.toolbar_detail);

        setSupportActionBar(toolbar_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Register Account");

        birthday_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                birthdayCalendar.set(Calendar.YEAR, year);
                                birthdayCalendar.set(Calendar.MONTH, monthOfYear);
                                birthdayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                birthday_input.setText(Util.calendarToString(birthdayCalendar, "dd MMM yyyy"));
                            }
                        },
                        birthdayCalendar.get(Calendar.YEAR),
                        birthdayCalendar.get(Calendar.MONTH),
                        birthdayCalendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.save:
                if(password_input.getText().toString().equals(repassword_input.getText().toString())) {
                    apiService.register(
                            username_input.getText().toString(),
                            password_input.getText().toString(),
                            name_input.getText().toString(),
                            Util.calendarToString(birthdayCalendar, "yyyy-MM-dd"),
                            gender_input.getText().toString()
                    ).enqueue(new RegisterCallback());

                    Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Password and Re-password is not correct", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class RegisterCallback implements Callback<Auth>{

        @Override
        public void onResponse(Call<Auth> call, Response<Auth> response) {
            if (response.isSuccessful()) {
                Profile profile = response.body().getUser();
                preferencesHelper.setUserLogin(profile, response.body().getMessage());
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
                finish();
            }
            Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onFailure(Call<Auth> call, Throwable t) {

        }
    }
}
