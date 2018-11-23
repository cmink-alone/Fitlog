package com.example.trio.triathlog;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
