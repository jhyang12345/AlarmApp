package com.arrata.user.alarmapp;

import android.content.Context;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.realm.Realm;

public class AlarmActive extends AppCompatActivity {

    AudioManager audioManager;
    static int originalVolume;

    Realm myRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_active);

        AudioPlayer.stop();
        AudioPlayer.play(this, R.raw.epilogue);

        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
    }


    @Override
    protected void onDestroy() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
        super.onDestroy();
    }
}
