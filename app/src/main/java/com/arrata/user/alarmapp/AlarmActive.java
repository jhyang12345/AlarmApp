package com.arrata.user.alarmapp;

import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import io.realm.Realm;

public class AlarmActive extends AppCompatActivity {

    AudioManager audioManager;
    static int originalVolume;

    Button stopButton;

    Vibrator vibrator;

    Realm myRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_active);

        AudioPlayer.stop();
        AudioPlayer.play(this, R.raw.epilogue);

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

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        long pattern[] = {1000, 1000, 1000, 1000, 1000};
        vibrator.vibrate(pattern, 1);
    }


    @Override
    protected void onDestroy() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
        stopAlarm();
        super.onDestroy();
    }

    void stopAlarm() {
        vibrator.cancel();
        AudioPlayer.stop();

    }
}
