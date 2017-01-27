package com.arrata.user.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class AlarmActive extends AppCompatActivity {

    AudioManager audioManager;
    static int originalVolume;

    Button stopButton;

    TextView alarmMessage;

    TextView alarmTime;
    TextView alarmAMPM;

    Alarm alarm;

    static int code;
    static int hours;
    static int minutes;
    static String message;

    AlarmManager alarmManager;

    Vibrator vibrator;

    Realm myRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_active);

        alarmMessage = (TextView) findViewById(R.id.alarmMessage);
        alarmTime = (TextView) findViewById(R.id.alarmTime);
        alarmAMPM = (TextView) findViewById(R.id.alarmAmPm);

        AudioPlayer.stop();
        AudioPlayer.play(this, R.raw.lovely);

        myRealm = Realm.getInstance(this);

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        stopButton = (Button) findViewById(R.id.alarm_off);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAlarm();
                finish();
            }
        });

        Log.d("AlarmActive", String.valueOf(getIntent().getIntExtra("code", 0)));

        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        this.setVolumeControlStream(AudioManager.STREAM_ALARM);
        originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0);
        long pattern[] = {1000, 1000, 1000, 1000, 1000};
        vibrator.vibrate(pattern, 1);

        initializeAlarm();

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        int[] gC = new SettingsActivity().generateCode(code);
        Calendar current = Calendar.getInstance();

        Intent alarmIntent = new Intent(AlarmActive.this, AlarmReceiver.class);

        alarmIntent.putExtra("code", code);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmActive.this, gC[current.get(Calendar.DAY_OF_WEEK)], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        setAlarm(pendingIntent, code, hours, minutes, current.get(Calendar.DAY_OF_WEEK));


        alarmMessage.setText(message);
        if(message.equals("")) alarmMessage.setText("알람");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            DateFormatSymbols symbols = new DateFormatSymbols(Locale.US);
            sdf.setDateFormatSymbols(symbols);
            final Date dateObj = sdf.parse(String.valueOf(hours) + ":" + String.valueOf(minutes));

            SimpleDateFormat next = new SimpleDateFormat("K:mm a");
            next.setDateFormatSymbols(symbols);

            alarmTime.setText(next.format(dateObj).substring(0, next.format(dateObj).length() - 3));
            alarmAMPM.setText(next.format(dateObj).substring(next.format(dateObj).length() - 2, next.format(dateObj).length()));

        } catch (ParseException e) {

            alarmTime.setText("");
            alarmAMPM.setText("");
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, originalVolume, 0);
        stopAlarm();
        super.onDestroy();
    }

    void stopAlarm() {
        vibrator.cancel();
        AudioPlayer.stop();

    }

    void initializeAlarm() {
        Intent intent = getIntent();
        code = intent.getIntExtra("code", 0);
        alarm = myRealm.where(Alarm.class)
                .equalTo("code", code)
                .findFirst();
        if(alarm == null || code == 0) {
            alarm = myRealm.where(Alarm.class)
                    .findFirst();
        }
        hours = alarm.getHours();
        minutes = alarm.getMinutes();
        message = alarm.getMessage();
    }

    void setAlarm(PendingIntent pendingIntent, int code, int hours, int minutes, int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        Calendar current = Calendar.getInstance();
        current.getTimeInMillis();

        long trigger = 0;
        if(current.getTimeInMillis() >= calendar.getTimeInMillis()) {
            trigger = calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY * 7;
        } else {
            trigger = calendar.getTimeInMillis();
        }

        System.out.println("Trigger from active alarm " + trigger + " " + current.getTimeInMillis());

        if(Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
            //    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000,pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
            //    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000,pendingIntent);
        }
        Log.d("setAlarm", "alarm set at " + calendar.toString());

    }
}
