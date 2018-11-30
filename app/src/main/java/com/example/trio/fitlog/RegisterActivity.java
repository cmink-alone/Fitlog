package com.example.trio.fitlog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.trio.fitlog.utils.Util;

public class RegisterActivity extends AppCompatActivity {

    private Toolbar toolbar_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Util.whiteStatusBar(this);

        toolbar_detail = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Register Account");


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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
