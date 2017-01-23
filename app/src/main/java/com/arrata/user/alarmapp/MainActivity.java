package com.arrata.user.alarmapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    Realm myRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRealm = Realm.getInstance(this);

        int startcode = 0;
        int count = 0;
        RealmResults<Alarm> results = myRealm.where(Alarm.class).findAll();
        if(results.size() == 0) {
            startcode = 0;
        } else {
            startcode = getNextKey();
        }

        myRealm.beginTransaction();

        Alarm alarm1 = myRealm.createObject(Alarm.class);
        alarm1.setCode(startcode);

        myRealm.commitTransaction();

        RealmResults<Alarm> results2 =
                myRealm.where(Alarm.class)
                        .findAll();
        for(int i = 0; i < results2.size(); ++i) {
            Log.d("Alarms", String.valueOf(results2.get(i).getHours()));
        }
        Log.d("Alarms", String.valueOf(results2.size()));
    }

    public int getNextKey() {
        return myRealm.where(Alarm.class).max("code").intValue() + 1;
    }
}
