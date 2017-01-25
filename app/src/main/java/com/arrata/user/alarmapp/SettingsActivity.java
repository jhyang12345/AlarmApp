package com.arrata.user.alarmapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    EditText mHours;
    EditText mMinutes;
    

    TextView mMonday;
    TextView mTuesday;
    TextView mWednesday;
    TextView mThursday;
    TextView mFriday;
    TextView mSaturday;
    TextView mSunday;

    static boolean mon = false;
    static boolean tue = false;
    static boolean wed = false;
    static boolean thu = false;
    static boolean fri = false;
    static boolean sat = false;
    static boolean sun = false;

    static boolean adding = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_settings);

        mHours = (EditText) findViewById(R.id.hours);
        mMinutes = (EditText) findViewById(R.id.minutes);

        mMonday = (TextView) findViewById(R.id.monday);
        mTuesday = (TextView) findViewById(R.id.tuesday);
        mWednesday = (TextView) findViewById(R.id.wednesday);
        mThursday = (TextView) findViewById(R.id.thursday);
        mFriday = (TextView) findViewById(R.id.friday);
        mSaturday = (TextView) findViewById(R.id.saturday);
        mSunday = (TextView) findViewById(R.id.sunday);

        ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null);

        Intent intent = getIntent();
        String role = intent.getStringExtra("role");
        if(role.equals("add")) adding = true;
        else adding = false;

        Log.d("SettingsActivity role", String.valueOf(adding));

        mHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 2) {
                    mHours.setText(s.subSequence(0, 2));
                }
                if(s.toString().length() > 0 && Integer.valueOf(s.toString()) > 23) {
                    mHours.setText(String.valueOf(23));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
/*
        mHours.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(v.etTe.toString().length() == 0) {
                        mHours.setText("00");
                    }
                    if(s.toString().length() < 2) {
                        mHours.setText("0" + s.toString());
                    }
                }
            }
        });*/

        mMinutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 2) {
                    mMinutes.setText(s.subSequence(0, 2));
                }
                if(s.toString().length() > 0 && Integer.valueOf(s.toString()) > 59) {
                    mMinutes.setText(String.valueOf(59));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mon) {
                    mMonday.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
                    mMonday.setTextColor(Color.WHITE);
                    mon = true;
                } else {
                    mMonday.setBackgroundColor(0);
                    mMonday.setTextColor(Color.BLACK);
                }
            }
        });

        mTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tue) {
                    mTuesday.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
                    mTuesday.setTextColor(Color.WHITE);
                    tue = true;
                } else {
                    mTuesday.setBackgroundColor(0);
                    mTuesday.setTextColor(Color.BLACK);
                    tue = false;
                }
            }
        });

        mWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!wed) {
                    mWednesday.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
                    mWednesday.setTextColor(Color.WHITE);
                    wed = true;
                } else {
                    mWednesday.setBackgroundColor(0);
                    mWednesday.setTextColor(Color.BLACK);
                    wed = false;
                }
            }
        });
        mThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!thu) {
                    mThursday.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
                    mThursday.setTextColor(Color.WHITE);
                    thu = true;
                } else {
                    mThursday.setBackgroundColor(0);
                    mThursday.setTextColor(Color.BLACK);
                    thu = false;
                }
            }
        });
        mFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fri) {
                    mFriday.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
                    mFriday.setTextColor(Color.WHITE);

                    fri = true;
                } else {
                    mFriday.setBackgroundColor(0);
                    mFriday.setTextColor(Color.BLACK);
                    fri = false;
                }
            }
        });
        mSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sat) {
                    mSaturday.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
                    mSaturday.setTextColor(Color.WHITE);
                    sat = true;
                } else {
                    mSaturday.setBackgroundColor(0);
                    mSaturday.setTextColor(Color.BLACK);
                    sat = false;
                }
            }
        });
        mSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sun) {
                    mSunday.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
                    mSunday.setTextColor(Color.WHITE);
                    sun = true;
                } else {
                    mSunday.setBackgroundColor(0);
                    mSunday.setTextColor(Color.BLACK);
                    sun = false;
                }
            }
        });


    }

}
