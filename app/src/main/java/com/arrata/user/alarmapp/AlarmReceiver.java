package com.arrata.user.alarmapp;

import android.content.Context;
import android.content.Intent;
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

        }

    }

}
