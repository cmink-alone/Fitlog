package com.example.trio.triathlog;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trio.triathlog.database.TriathlogDbHelper;
import com.example.trio.triathlog.model.Activity;
import com.example.trio.triathlog.model.Profile;
import com.example.trio.triathlog.model.Type;
import com.example.trio.triathlog.util.Util;

import java.util.Calendar;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {
    private final int REQUEST_UPDATE = 1;

    TextView title;
    ImageView icon_activity;
    TextView date;
    TextView move_time;
    TextView move_minutes;
    TextView distance;
    TextView heart_points;
    TextView calories;
    TextView speed;

    private Toolbar toolbar_detail;

    Activity activity;

    private void populateData(Activity activity){
        Calendar calendar = Util.stringToCalendar(activity.getDatetime(), "yyyy-MM-dd HH:mm");
        Calendar calendar2 = Util.stringToCalendar(activity.getDatetime(), "yyyy-MM-dd HH:mm");
        String pattern = "dd MMMM";
        if(calendar.get(Calendar.YEAR) != Calendar.getInstance().get(Calendar.YEAR))
            pattern+=" yyyy";

        calendar2.add(Calendar.HOUR, activity.getHour());
        calendar2.add(Calendar.MINUTE, activity.getMinute());

        Type type = TriathlogDbHelper.getInstance(this).getType(activity.getType_id());
        Profile profile = TriathlogDbHelper.getInstance(this).getProfile(1);

        //title.setText(activity.getTitle());

        setTitle(activity.getTitle());

        icon_activity.setImageResource(type.getIcon());
        date.setText(Util.calendarToStringFriendly(calendar, pattern) + ", " + Util.calendarToString(calendar, "HH:mm") + " - " + Util.calendarToString(calendar2, "HH:mm"));
        move_time.setText(activity.getHour() +"hr " + activity.getMinute() + "min");
        move_minutes.setText(activity.getTotalMinutes() + " min");
        distance.setText(activity.getDistance() + " km");
        speed.setText(String.format("%.2f",activity.getSpeed()) + " km/h");
        heart_points.setText(String.valueOf(activity.getHeartPoint()));
        calories.setText(String.valueOf(activity.getCaloriesBurned(profile)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));

        toolbar_detail = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //finding views
        title = findViewById(R.id.title);
        icon_activity = findViewById(R.id.icon_activity);
        date = findViewById(R.id.date);
        move_time = findViewById(R.id.move_time);
        move_minutes = findViewById(R.id.move_minutes);
        distance = findViewById(R.id.distance);
        heart_points = findViewById(R.id.heart_points);
        calories = findViewById(R.id.calories);
        speed = findViewById(R.id.speed);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        activity = (Activity) bundle.getSerializable("Activity");

        populateData(activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.edit:

                Intent add_activity = new Intent(getApplicationContext(), AddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Activity", activity);
                add_activity.putExtras(bundle);
                this.startActivityForResult(add_activity, REQUEST_UPDATE);

                return true;
            case R.id.delete:
                new AlertDialog.Builder(this)
                        .setTitle("Delete Activity")
                        .setMessage("Do you really want to delete this activity?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                TriathlogDbHelper.getInstance(getApplicationContext()).deleteActivity(activity);
                                finish();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle = data.getExtras();

        activity = (Activity) bundle.getSerializable("Activity");

        populateData(activity);
    }
}
