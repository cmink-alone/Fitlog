package com.example.trio.triathlog;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trio.triathlog.database.TriathlogDbHelper;
import com.example.trio.triathlog.model.Profile;
import com.example.trio.triathlog.util.Util;

import java.util.Calendar;
import java.util.Date;


public class ProfileFragment extends Fragment {
    private Profile profile;
    private Calendar birthdayCalendar = Calendar.getInstance();
    private ImageView profile_pict;
    private EditText name_input;
    private EditText move_minute_input;
    private EditText move_distance_input;
    private EditText gender_input;
    private EditText birthday_input;
    private EditText weight_input;
    private EditText height_input;

    public ProfileFragment() {}

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");

        profile = TriathlogDbHelper.getInstance(getActivity()).getProfile(1);

        name_input = getActivity().findViewById(R.id.name_input);
        move_minute_input = getActivity().findViewById(R.id.move_minute_input);
        move_distance_input = getActivity().findViewById(R.id.move_distance_input);
        gender_input = getActivity().findViewById(R.id.gender_input);
        birthday_input = getActivity().findViewById(R.id.birthday_input);
        weight_input = getActivity().findViewById(R.id.weight_input);
        height_input = getActivity().findViewById(R.id.height_input);
        profile_pict = getActivity().findViewById(R.id.profile_pict);

        Glide.with(this).load(R.drawable.profile).into(profile_pict);

        birthday_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                birthdayCalendar.set(Calendar.YEAR, year);
                                birthdayCalendar.set(Calendar.MONTH, monthOfYear);
                                birthdayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                birthday_input.setText(Util.calendarToString(birthdayCalendar, "dd MMM yyyy"));
                                profile.setBirthday(Util.calendarToString(birthdayCalendar, "yyyy-MM-dd"));
                                TriathlogDbHelper.getInstance(getActivity()).editProfile(profile);
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

        name_input.setOnFocusChangeListener(new FocusChangeListener());
        move_minute_input.setOnFocusChangeListener(new FocusChangeListener());
        move_distance_input.setOnFocusChangeListener(new FocusChangeListener());
        gender_input.setOnFocusChangeListener(new FocusChangeListener());
        birthday_input.setOnFocusChangeListener(new FocusChangeListener());
        weight_input.setOnFocusChangeListener(new FocusChangeListener());
        height_input.setOnFocusChangeListener(new FocusChangeListener());

        populateData(profile);
    }

    private void populateData(Profile profile){
        if(profile.getName()!=null) {
            name_input.setText(profile.getName());
        }
        if(profile.getMove_minutes()!=0) {
            move_minute_input.setText(String.valueOf(profile.getMove_minutes()));
        }
        if(profile.getMove_distance()!=0) {
            move_distance_input.setText(String.valueOf(profile.getMove_distance()));
        }
        if(profile.getGender()!=null) {
            gender_input.setText(profile.getGender());
        }
        if(profile.getWeight()!=0) {
            weight_input.setText(String.valueOf(profile.getWeight()));
        }
        if(profile.getHeight()!=0) {
            height_input.setText(String.valueOf(profile.getHeight()));
        }
    }

    class FocusChangeListener implements View.OnFocusChangeListener{
        @Override
        public void onFocusChange(View view, boolean b) {
            if(!b) {
                boolean changed = false;
                if(!name_input.getText().toString().equals(profile.getName())) {
                    profile.setName(name_input.getText().toString());
                    changed=true;
                }
                if(!move_minute_input.getText().toString().equals("")){
                    if(Integer.parseInt(move_minute_input.getText().toString()) != profile.getMove_minutes()) {
                        profile.setMove_minutes(Integer.parseInt(move_minute_input.getText().toString()));
                        changed=true;
                    }
                }
                if(!move_distance_input.getText().toString().equals("")) {
                    if (Integer.parseInt(move_distance_input.getText().toString()) != profile.getMove_distance()) {
                        profile.setMove_distance(Integer.parseInt(move_distance_input.getText().toString()));
                        changed = true;
                    }
                }
                if(!gender_input.getText().toString().equals(profile.getGender())) {
                    profile.setGender(gender_input.getText().toString());
                    changed=true;
                }
                if(!birthday_input.getText().toString().equals(profile.getBirthday()) && !birthday_input.getText().toString().equals("")) {
                    profile.setBirthday(Util.calendarToString(birthdayCalendar, "yyyy-MM-dd"));
                    changed=true;
                }
                if(!weight_input.getText().toString().equals("")) {
                    if (Integer.parseInt(weight_input.getText().toString()) != profile.getWeight()) {
                        profile.setWeight(Integer.parseInt(weight_input.getText().toString()));
                        changed = true;
                    }
                }
                if(!height_input.getText().toString().equals("")) {
                    if (Integer.parseInt(height_input.getText().toString()) != profile.getHeight()) {
                        profile.setHeight(Integer.parseInt(height_input.getText().toString()));
                        changed = true;
                    }
                }
                if(changed) {
                    TriathlogDbHelper.getInstance(getActivity()).editProfile(profile);
                }
            }
        }
    }

}
