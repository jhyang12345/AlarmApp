package com.arrata.user.alarmapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.R.id.closeButton;

/**
 * Created by user on 2017-01-23.
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    RealmResults<Alarm> alarms;
    Context context;
    Realm myRealm;

    public AlarmAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_alarm, parent, false);
        ViewHolder vh = new ViewHolder(v);
        myRealm = Realm.getInstance(context);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Alarm alarm = alarms.get(position);
        String hour = "";
        if(String.valueOf(alarm.getHours()).length() < 2) {
            hour = "0" + String.valueOf(alarm.getHours());
        } else {
            hour = String.valueOf(alarm.getHours());
        }
        String minute = "";
        if(String.valueOf(alarm.getMinutes()).length() < 2) {
            minute = "0" + String.valueOf(alarm.getMinutes());
        } else {
            minute = String.valueOf(alarm.getMinutes());
        }
        holder.alarmTime.setText(hour + ":" + minute);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            DateFormatSymbols symbols = new DateFormatSymbols(Locale.US);
            sdf.setDateFormatSymbols(symbols);
            final Date dateObj = sdf.parse(hour + ":" + minute);
            System.out.println(dateObj);
            SimpleDateFormat next = new SimpleDateFormat("K:mm a");
            next.setDateFormatSymbols(symbols);

            holder.ampm.setText(next.format(dateObj).substring(next.format(dateObj).length() - 2, next.format(dateObj).length()));
            System.out.println(next.format(dateObj));
        } catch (ParseException e) {

            holder.ampm.setText("");
            e.printStackTrace();
        }



        if(alarm.getMessage() == null || alarm.getMessage().length() == 0) {
            holder.alarmMessage.setText("알람");
        } else {
            holder.alarmMessage.setText(alarm.getMessage());
        }

        holder.closeButton.setTag(position);

        holder.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                Log.d("Adapter", String.valueOf(position));
                Log.d("Adapter", String.valueOf(alarms.get(position).getCode()));

                int code = alarms.get(position).getCode();
                RealmResults<Alarm> results =
                        myRealm.where(Alarm.class)
                                .equalTo("code", code)
                                .findAll();
                myRealm.beginTransaction();
                results.clear();
                myRealm.commitTransaction();


                notifyItemRemoved(position);
                notifyItemRangeChanged(position, alarms.size());

            }
        });

        holder.activeIcon.setTag(position);

        activate((ImageView) holder.activeIcon, alarm.isActive());

        holder.activeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();

                int code = alarms.get(position).getCode();
                Alarm alarm = myRealm.where(Alarm.class)
                        .equalTo("code", code)
                        .findFirst();

                myRealm.beginTransaction();
                alarm.setActive(!alarm.isActive());
                myRealm.commitTransaction();

                alarm = myRealm.where(Alarm.class)
                        .equalTo("code", code)
                        .findFirst();

                activate((ImageView) v, alarm.isActive());

            }
        });

        if(alarm.isMonday()) selectDay(holder.mon);
        if(alarm.isTuesday()) selectDay(holder.tue);
        if(alarm.isWednesday()) selectDay(holder.wed);
        if(alarm.isThursday()) selectDay(holder.thu);
        if(alarm.isFriday()) selectDay(holder.fri);
        if(alarm.isSaturday()) selectDay(holder.sat);
        if(alarm.isSunday()) selectDay(holder.sun);

        holder.wholeAlarm.setTag(alarm.getCode());
        holder.wholeAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("WholeAlarm", "clicked!");
                Intent intent = new Intent(context, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("role", "edit");
                intent.putExtra("code", (int) v.getTag());
                context.startActivity(intent);
            }
        });
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
        private TextView ampm;
        private TextView alarmMessage;
        private ImageView closeButton;
        private ImageView activeIcon;
        private TextView mon;
        private TextView tue;
        private TextView wed;
        private TextView thu;
        private TextView fri;
        private TextView sat;
        private TextView sun;

        private RelativeLayout wholeAlarm;


        public ViewHolder(View v) {
            super(v);
            wholeAlarm = (RelativeLayout) v.findViewById(R.id.whole_alarm);
            alarmTime = (TextView) v.findViewById(R.id.alarmtime);
            ampm = (TextView) v.findViewById(R.id.ampm);
            alarmMessage = (TextView) v.findViewById(R.id.message);
            closeButton = (ImageView) v.findViewById(R.id.close_button);
            activeIcon = (ImageView) v.findViewById(R.id.alarm_icon);
            mon = (TextView) v.findViewById(R.id.monday);
            tue = (TextView) v.findViewById(R.id.tuesday);
            wed = (TextView) v.findViewById(R.id.wednesday);
            thu = (TextView) v.findViewById(R.id.thursday);
            fri = (TextView) v.findViewById(R.id.friday);
            sat = (TextView) v.findViewById(R.id.saturday);
            sun = (TextView) v.findViewById(R.id.sunday);
        }

    }

    void selectDay(TextView v) {
        v.setBackgroundResource(R.drawable.selected_background);
        v.setTextColor(Color.WHITE);
    }

    void activate(ImageView v, boolean active) {
        if(active) {
            v.setAlpha((float) 1.0);
        } else {
            v.setAlpha((float) 0.1);
        }
    }
}
