package com.arrata.user.alarmapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    EditText mHours;
    EditText mMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_settings);

        mHours = (EditText) findViewById(R.id.hours);
        mMinutes = (EditText) findViewById(R.id.minutes);

        mHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 2) {
                    mHours.setText(s.subSequence(0, 2));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mMinutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 2) {
                    mMinutes.setText(s.subSequence(0, 2));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
 //       setSupportActionBar(toolbar);
    }

}
