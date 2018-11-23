package com.example.trio.triathlog;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_container, HomeFragment.newInstance(), HomeFragment.class.getSimpleName())
                                .commit();

                        break;
                    case R.id.nav_activities:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_container, ActivitiesFragment.newInstance(), ActivitiesFragment.class.getSimpleName())
                                .commit();
                        break;
                    case R.id.nav_profile:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_container, ProfileFragment.newInstance(), ProfileFragment.class.getSimpleName())
                                .commit();
                        break;
                }
                return true;
            }

        });
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

    }
}
