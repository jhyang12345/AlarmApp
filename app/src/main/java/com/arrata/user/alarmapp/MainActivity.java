package com.arrata.user.alarmapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    Realm myRealm;

    RecyclerView mRecyclerView;

    AlarmAdapter alarmAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        myRealm = Realm.getInstance(this);

        long time = System.currentTimeMillis();

        myRealm.beginTransaction();

        Alarm alarm1 = myRealm.createObject(Alarm.class);
        alarm1.setCode(time);

        myRealm.commitTransaction();

        RealmResults<Alarm> results2 =
                myRealm.where(Alarm.class)
                        .findAll();

        alarmAdapter = new AlarmAdapter(this);
        mRecyclerView.setAdapter(alarmAdapter);

        alarmAdapter.setItems(results2);

    }


    public int getNextKey() {
        return myRealm.where(Alarm.class).max("code").intValue() + 1;
    }
}
