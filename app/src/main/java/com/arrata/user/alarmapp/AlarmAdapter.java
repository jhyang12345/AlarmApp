package com.arrata.user.alarmapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import io.realm.RealmResults;

/**
 * Created by user on 2017-01-23.
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    RealmResults<Alarm> alarms;
    Context context;

    public AlarmAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_alarm, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Alarm alarm = alarms.get(position);
        String hour = "";
        if(String.valueOf(alarm.getHours()).length() < 2) {
            hour = "0" + String.valueOf(alarm.getHours());
        }
        String minute = "";
        if(String.valueOf(alarm.getMinutes()).length() < 2) {
            minute = "0" + String.valueOf(alarm.getMinutes());
        }
        holder.alarmTime.setText(hour + ":" + minute);

    }

    @Override
    public int getItemCount() {
        if(alarms == null) return 0;
        return alarms.size();
    }

    public void setItems(RealmResults<Alarm> alarms) {
        this.alarms = alarms;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView alarmTime;
        private ImageView closeButton;


        public ViewHolder(View v) {
            super(v);
            alarmTime = (TextView) v.findViewById(R.id.alarmtime);
            closeButton = (ImageView) v.findViewById(R.id.close_button);

        }


    }
}
