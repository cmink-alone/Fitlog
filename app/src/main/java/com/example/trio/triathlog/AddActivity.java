package com.example.trio.triathlog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.trio.triathlog.database.TriathlogDbHelper;
import com.example.trio.triathlog.model.Activity;
import com.example.trio.triathlog.model.Type;
import com.example.trio.triathlog.util.Util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    EditText title;
    ImageView icon_activity;
    Spinner type;
    EditText distance;
    EditText hour;
    EditText minute;
    EditText date;
    EditText time;
    EditText desc;
    Calendar myCalendar;
    MenuItem save;
    Activity activity;

    boolean isAdd = true;

    public void populateData(Activity activity){
        title.setText(activity.getTitle());
        distance.setText(String.valueOf(activity.getDistance()));
        hour.setText(String.valueOf(activity.getHour()));
        minute.setText(String.valueOf(activity.getMinute()));
        desc.setText(activity.getDescription());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //load all data from sqlite
        final List<Type> types = TriathlogDbHelper.getInstance(this).getAllType();

        //finding views
        title = findViewById(R.id.title_input);
        icon_activity = findViewById(R.id.icon_activity);
        type = findViewById(R.id.type_input);
        distance = findViewById(R.id.distance_input);
        hour = findViewById(R.id.hour_input);
        minute = findViewById(R.id.minute_input);
        date = findViewById(R.id.date_input);
        time = findViewById(R.id.time_input);
        desc = findViewById(R.id.desc_input);

        //isntance Calendar
        myCalendar = Calendar.getInstance();

        //spinner
        ArrayAdapter<Type> adapter = new ArrayAdapter<Type>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);

        activity = new Activity();
        try {
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();

            activity = (Activity) bundle.getSerializable("Activity");
            isAdd = false;
        } catch (Exception e){

        }

        if(isAdd){
            setTitle("Add Activity");
        } else {
            setTitle("Edit Activity");
            myCalendar = Util.stringToCalendar(activity.getDatetime(),"yyyy-MM-dd HH:mm");

            Type type = TriathlogDbHelper.getInstance(this).getType(activity.getType_id());

            int type_position = adapter.getPosition(type);
            this.type.setSelection(type_position);

            populateData(activity);
        }

        date.setText(Util.calendarToStringFriendly(myCalendar, "yyyy-MM-dd"));
        time.setText(Util.calendarToString(myCalendar, "HH:mm"));

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                icon_activity.setImageResource(types.get(i).getIcon());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //display datepicker
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                date.setText(Util.calendarToStringFriendly(myCalendar, "yyyy-MM-dd"));
                            }
                        },
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        //display timepicker
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);

                new TimePickerDialog(
                        AddActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                myCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                                myCalendar.set(Calendar.MINUTE, selectedMinute);
                                time.setText(Util.calendarToString(myCalendar, "HH:mm"));
                            }
                        },
                        hour,
                        minute,
                        true
                ).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        save = menu.findItem(R.id.save);
        save.setEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.save:
                Type selectedType = (Type) type.getSelectedItem();
                activity.setTitle(title.getText().toString());
                activity.setType_id(selectedType.getId());
                activity.setDistance(Integer.parseInt(distance.getText().toString()));
                activity.setHour(Integer.parseInt(hour.getText().toString()));
                activity.setMinute(Integer.parseInt(minute.getText().toString()));
                activity.setDatetime(Util.calendarToString(myCalendar, "yyyy-MM-dd HH:mm"));
                activity.setDescription(desc.getText().toString());
                if(isAdd) {
                    TriathlogDbHelper.getInstance(this).insertActivity(activity);
                } else {
                    TriathlogDbHelper.getInstance(this).editActivity(activity);


                    Intent add_activity = new Intent(getApplicationContext(), AddActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Activity", activity);
                    add_activity.putExtras(bundle);
                    setResult(RESULT_OK, add_activity);
                }
                finish();
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
    }

}
