package com.arrata.user.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Jae Hyeong on 2017-01-28.
 */

public class BootReceiver extends BroadcastReceiver {

    Realm myRealm;
    int code;
    int hours;
    int minutes;

    @Override
    public void onReceive(Context context, Intent intent) {
        myRealm = Realm.getInstance(context);

        RealmResults<Alarm> alarms = myRealm
                .where(Alarm.class)
                .findAll();
        for(int i = 0; i < alarms.size(); ++i) {
            Alarm alarm = alarms.get(i);
            if(!alarm.isActive()) continue;

            code = alarm.getCode();

            SettingsActivity st = new SettingsActivity();
            int[] gC = st.generateCode(code);

            hours = alarm.getHours();
            minutes = alarm.getMinutes();

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

        Calendar current = Calendar.getInstance();
        current.getTimeInMillis();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long trigger = 0;
        if(current.getTimeInMillis() > calendar.getTimeInMillis()) {
            trigger = calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY * 7;
        } else {
            trigger = calendar.getTimeInMillis();
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
