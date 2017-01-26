package com.arrata.user.alarmapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by user on 2017-01-27.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {
    AudioPlayer audioPlayer;


    @Override
    public void onReceive(Context context, Intent intent) {
        audioPlayer = new AudioPlayer();
        //audioPlayer.play(context, R.raw.epilogue);

        Log.d("AlarmReceiver", "called");

        Intent service = new Intent(context, AlarmIntentService.class);
        service.putExtra("code", intent.getIntExtra("code", 0));
        startWakefulService(context, service);

    }

}
