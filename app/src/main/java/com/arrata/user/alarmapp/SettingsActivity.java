package com.arrata.user.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


import io.realm.Realm;

public class SettingsActivity extends AppCompatActivity {

    EditText mHours;
    EditText mMinutes;

    EditText mMessage;

    TextView mMonday;
    TextView mTuesday;
    TextView mWednesday;
    TextView mThursday;
    TextView mFriday;
    TextView mSaturday;
    TextView mSunday;

    Button mSave;

    TimePicker timePicker;

    static int hours;
    static int minutes;

    static boolean mon = false;
    static boolean tue = false;
    static boolean wed = false;
    static boolean thu = false;
    static boolean fri = false;
    static boolean sat = false;
    static boolean sun = false;

    static boolean adding = false;

    static int code;

    static String message = "";

    Realm myRealm;

    AlarmManager alarmManager;

    void editInit(int code) {
        Alarm alarm = myRealm.where(Alarm.class)
                .equalTo("code", code)
                .findFirst();
        mon = alarm.isMonday();
        tue = alarm.isTuesday();
        wed = alarm.isWednesday();
        thu = alarm.isThursday();
        fri = alarm.isFriday();
        sat = alarm.isSaturday();
        sun = alarm.isSunday();
        message = alarm.getMessage();

        hours = alarm.getHours();
        minutes = alarm.getMinutes();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_settings);

        mMessage = (EditText) findViewById(R.id.message);

        mMonday = (TextView) findViewById(R.id.monday);
        mTuesday = (TextView) findViewById(R.id.tuesday);
        mWednesday = (TextView) findViewById(R.id.wednesday);
        mThursday = (TextView) findViewById(R.id.thursday);
        mFriday = (TextView) findViewById(R.id.friday);
        mSaturday = (TextView) findViewById(R.id.saturday);
        mSunday = (TextView) findViewById(R.id.sunday);

        mSave = (Button) findViewById(R.id.save);

        timePicker = (TimePicker) findViewById(R.id.timePicker);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        myRealm = Realm.getInstance(this);

        Intent intent = getIntent();
        String role = intent.getStringExtra("role");
        if(role.equals("add")) adding = true;
        else adding = false;

        Log.d("SettingsActivity role", String.valueOf(adding));
        if(!adding) {
            code = intent.getIntExtra("code", 0);
            Log.d("SettingActivity code", String.valueOf(code));
            editInit(code);
            if(Build.VERSION.SDK_INT >= 23 ) {
                timePicker.setHour(hours);
                timePicker.setMinute(minutes);
            } else {
                timePicker.setCurrentHour(hours);
                timePicker.setCurrentMinute(minutes);
            }
            mMessage.setText(message);
            setDaySelected(mMonday, mon);
            setDaySelected(mTuesday, tue);
            setDaySelected(mWednesday, wed);
            setDaySelected(mThursday, thu);
            setDaySelected(mFriday, fri);
            setDaySelected(mSaturday, sat);
            setDaySelected(mSunday, sun);
        } else {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            Log.d("WeekDay", String.valueOf(day));
            Log.d("WeekDay", String.valueOf(Calendar.FRIDAY));
            switch(day) {
                case Calendar.SUNDAY: {
                    sun = true;
                    setDaySelected(mSunday, sun);
                    break;
                } case Calendar.MONDAY: {
                    mon = true;
                    setDaySelected(mMonday, mon);
                    break;
                }case Calendar.TUESDAY: {
                    tue = true;
                    setDaySelected(mTuesday, tue);
                    break;
                }case Calendar.WEDNESDAY: {
                    wed = true;
                    setDaySelected(mWednesday, wed);
                    break;
                }case Calendar.THURSDAY: {
                    thu = true;
                    setDaySelected(mThursday, thu);
                    break;
                }case Calendar.FRIDAY: {
                    fri = true;
                    setDaySelected(mFriday, fri);
                    break;
                }case Calendar.SATURDAY: {
                    sat = true;
                    setDaySelected(mSaturday, sat);
                    break;
                }
            }
        }

        mMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mon) {
                    mMonday.setBackgroundResource(R.drawable.selected_background);
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
                    mTuesday.setBackgroundResource(R.drawable.selected_background);
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
                    mWednesday.setBackgroundResource(R.drawable.selected_background);
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
                    mThursday.setBackgroundResource(R.drawable.selected_background);
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
                    mFriday.setBackgroundResource(R.drawable.selected_background);
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
                    mSaturday.setBackgroundResource(R.drawable.selected_background);
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
                    mSunday.setBackgroundResource(R.drawable.selected_background);
                    mSunday.setTextColor(Color.WHITE);
                    sun = true;
                } else {
                    mSunday.setBackgroundColor(0);
                    mSunday.setTextColor(Color.BLACK);
                    sun = false;
                }
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23 ) {
                    hours = timePicker.getHour();
                    minutes = timePicker.getMinute();
                } else {
                    hours = timePicker.getCurrentHour();
                    minutes = timePicker.getCurrentHour();
                }
                Log.d("Current hour", String.valueOf(hours));

                long time = System.currentTimeMillis();

                message = mMessage.getText().toString();

                if(adding) {
                    myRealm.beginTransaction();

                    Alarm alarm = myRealm.createObject(Alarm.class);
                    alarm.setHours(hours);
                    alarm.setMinutes(minutes);
                    if(myRealm.where(Alarm.class).findFirst() == null) {
                        alarm.setCode(0);
                        code = 0;
                    } else {
                        code = getNextKey();
                        alarm.setCode(code);

                    }
                    alarm.setEdited(time);
                    alarm.setMonday(mon);
                    alarm.setTuesday(tue);
                    alarm.setWednesday(wed);
                    alarm.setThursday(thu);
                    alarm.setFriday(fri);
                    alarm.setSaturday(sat);
                    alarm.setSunday(sun);
                    alarm.setActive(true);
                    alarm.setMessage(message);
                    myRealm.commitTransaction();
                } else {
                    Alarm alarm = myRealm.where(Alarm.class)
                            .equalTo("code", code)
                            .findFirst();
                    myRealm.beginTransaction();
                    alarm.setHours(hours);
                    alarm.setMinutes(minutes);
                    alarm.setEdited(time);
                    alarm.setMonday(mon);
                    alarm.setTuesday(tue);
                    alarm.setWednesday(wed);
                    alarm.setThursday(thu);
                    alarm.setFriday(fri);
                    alarm.setSaturday(sat);
                    alarm.setSunday(sun);
                    alarm.setActive(true);
                    alarm.setMessage(message);
                    myRealm.commitTransaction();
                }

                //Intent alarmIntent = new Intent(SettingsActivity.this, AlarmReceiver.class);

                //alarmIntent.putExtra("code", code);
                //alarmIntent.setData(Uri.parse("custom://" + code));
                int[] gC = generateCode(code);
                for(int i = 0; i < 7; ++i) {
                    Log.d("GeneratedCode", String.valueOf(gC[i]));
                }

                //PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, code, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                //setAlarm(pendingIntent, code, hours, minutes, mon, tue, wed, thu, fri, sat, sun);
                //EXPERIMENT TO TEST CANCELLING
/*                for(int i = 0; i < 3; ++i) {
                    Intent alarmIntent = new Intent(SettingsActivity.this, AlarmReceiver.class);

                    alarmIntent.putExtra("code", code);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, gC[i], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hours);
                    calendar.set(Calendar.MINUTE, minutes + i);

                    if(Build.VERSION.SDK_INT >= 19) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }
                }*/

                //setting alarm for each day of week depending on boolean value
                if(mon) {
                    Intent alarmIntent = new Intent(SettingsActivity.this, AlarmReceiver.class);

                    alarmIntent.putExtra("code", code);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, gC[Calendar.MONDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    setAlarm(pendingIntent, code, hours, minutes, Calendar.MONDAY);
                }
                if(tue) {
                    Intent alarmIntent = new Intent(SettingsActivity.this, AlarmReceiver.class);

                    alarmIntent.putExtra("code", code);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, gC[Calendar.TUESDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    setAlarm(pendingIntent, code, hours, minutes, Calendar.TUESDAY);
                }
                if(wed) {
                    Intent alarmIntent = new Intent(SettingsActivity.this, AlarmReceiver.class);

                    alarmIntent.putExtra("code", code);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, gC[Calendar.WEDNESDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    setAlarm(pendingIntent, code, hours, minutes, Calendar.WEDNESDAY);
                }
                if(thu) {
                    Intent alarmIntent = new Intent(SettingsActivity.this, AlarmReceiver.class);

                    alarmIntent.putExtra("code", code);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, gC[Calendar.THURSDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    setAlarm(pendingIntent, code, hours, minutes, Calendar.THURSDAY);
                }
                if(fri) {
                    Intent alarmIntent = new Intent(SettingsActivity.this, AlarmReceiver.class);

                    alarmIntent.putExtra("code", code);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, gC[Calendar.FRIDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    setAlarm(pendingIntent, code, hours, minutes, Calendar.FRIDAY);
                }
                if(sat) {
                    Intent alarmIntent = new Intent(SettingsActivity.this, AlarmReceiver.class);

                    alarmIntent.putExtra("code", code);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, gC[Calendar.SATURDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    setAlarm(pendingIntent, code, hours, minutes, Calendar.SATURDAY);
                }
                if(sun) {
                    Intent alarmIntent = new Intent(SettingsActivity.this, AlarmReceiver.class);

                    alarmIntent.putExtra("code", code);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, gC[Calendar.SUNDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    setAlarm(pendingIntent, code, hours, minutes, Calendar.SUNDAY);
                }

                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        });

    }

    int[] generateCode(int code) {
        int[] codelist = {0, 0, 0, 0, 0, 0, 0, 0};
        String buffer = String.valueOf(code);
        for(int i = 0; i < 5; ++i) {
            if(buffer.length() > 4) break;
            buffer = buffer + "0";
        }
        for(int i = 0; i < 8; ++i) {
            codelist[i] = Integer.parseInt(buffer) + i;
        }
        return codelist;
    }

    void setAlarm(PendingIntent pendingIntent, int code, int hours, int minutes, int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        Calendar current = Calendar.getInstance();
        current.getTimeInMillis();

        long trigger = 0;
        if(current.getTimeInMillis() > calendar.getTimeInMillis()) {
            trigger = calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY * 7;
        } else {
            trigger = calendar.getTimeInMillis();
        }

        System.out.println("Trigger " + trigger + " " + current.getTimeInMillis());

        if(Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
            //    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000,pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
            //    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000,pendingIntent);
        }
        Log.d("setAlarm", "alarm set at " + calendar.toString());

    }

    void cancelAlarm(int alarmId) {
        Intent alarmIntent = new Intent(SettingsActivity.this, AlarmReceiver.class);

        Log.d("CancellingAlarm", String.valueOf(alarmId));
        //alarmIntent.putExtra("code", code);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, alarmId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    void setDaySelected(TextView v, boolean val) {
        if(val) {
            v.setBackgroundResource(R.drawable.selected_background);
            v.setTextColor(Color.WHITE);

        } else {
            v.setBackgroundColor(0);
            v.setTextColor(Color.BLACK);

        }
    }

    public int getNextKey() {
        return myRealm.where(Alarm.class).max("code").intValue() + 1;
    }

}
