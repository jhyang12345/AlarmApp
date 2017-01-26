package com.arrata.user.alarmapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    Realm myRealm;

    RecyclerView mRecyclerView;

    AlarmAdapter alarmAdapter;

    ImageView addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        addButton = (ImageView) findViewById(R.id.add_alarm);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("role", "add");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        myRealm = Realm.getInstance(this);

        long time = System.currentTimeMillis();


        RealmResults<Alarm> results2 =
                myRealm.where(Alarm.class)
                        .findAllSorted("edited", false);

        alarmAdapter = new AlarmAdapter(this);
        mRecyclerView.setAdapter(alarmAdapter);

        alarmAdapter.setItems(results2);

    }



}
