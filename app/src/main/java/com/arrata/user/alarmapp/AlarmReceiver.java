package com.arrata.user.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.sql.Time;
import java.util.Calendar;

import io.realm.Realm;

/**
 * Created by user on 2017-01-27.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {
    AudioPlayer audioPlayer;
    Realm myRealm;


    @Override
    public void onReceive(Context context, Intent intent) {
        audioPlayer = new AudioPlayer();
        //audioPlayer.play(context, R.raw.epilogue);

        int code = intent.getIntExtra("code", 0);

        myRealm = Realm.getInstance(context);

        Alarm alarm = myRealm.where(Alarm.class)
                .equalTo("code", code)
                .findFirst();

        int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minutes = Calendar.getInstance().get(Calendar.MINUTE);

        if(alarm.getHours() == hours && alarm.getMinutes() == minutes) {

            Log.d("AlarmReceiver", "called " + String.valueOf(code)) ;

            Intent service = new Intent(context, AlarmIntentService.class);
            service.putExtra("code", code);
            startWakefulService(context, service);

        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarm.getHours());
            calendar.set(Calendar.MINUTE, alarm.getMinutes());

            int gC[] = new SettingsActivity().generateCode(code);

            if(alarm.isMonday()) {
                Intent alarmIntent = new Intent(context, AlarmReceiver.class);

                alarmIntent.putExtra("code", code);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, gC[Calendar.MONDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                setAlarm(pendingIntent, code, hours, minutes, Calendar.MONDAY, context);
            }
            if(alarm.isTuesday()) {
                Intent alarmIntent = new Intent(context, AlarmReceiver.class);

                alarmIntent.putExtra("code", code);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, gC[Calendar.TUESDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                setAlarm(pendingIntent, code, hours, minutes, Calendar.TUESDAY, context);
            }
            if(alarm.isWednesday()) {
                Intent alarmIntent = new Intent(context, AlarmReceiver.class);

                alarmIntent.putExtra("code", code);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, gC[Calendar.WEDNESDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                setAlarm(pendingIntent, code, hours, minutes, Calendar.WEDNESDAY, context);
            }
            if(alarm.isThursday()) {
                Intent alarmIntent = new Intent(context, AlarmReceiver.class);

                alarmIntent.putExtra("code", code);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, gC[Calendar.THURSDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                setAlarm(pendingIntent, code, hours, minutes, Calendar.THURSDAY, context);
            }
            if(alarm.isFriday()) {
                Intent alarmIntent = new Intent(context, AlarmReceiver.class);

                alarmIntent.putExtra("code", code);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, gC[Calendar.FRIDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                setAlarm(pendingIntent, code, hours, minutes, Calendar.FRIDAY, context);
            }
            if(alarm.isSaturday()) {
                Intent alarmIntent = new Intent(context, AlarmReceiver.class);

                alarmIntent.putExtra("code", code);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, gC[Calendar.SATURDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                setAlarm(pendingIntent, code, hours, minutes, Calendar.SATURDAY, context);
            }
            if(alarm.isSunday()) {
                Intent alarmIntent = new Intent(context, AlarmReceiver.class);

                alarmIntent.putExtra("code", code);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, gC[Calendar.SUNDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                setAlarm(pendingIntent, code, hours, minutes, Calendar.SUNDAY, context);
            }

        }

    }

    void setAlarm(PendingIntent pendingIntent, int code, int hours, int minutes, int dayOfWeek, Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar current = Calendar.getInstance();
        current.getTimeInMillis();

        long trigger = 0 ;
        trigger = calendar.getTimeInMillis();
        while(trigger <= current.getTimeInMillis()) {
            trigger += AlarmManager.INTERVAL_DAY * 7;
        }

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
