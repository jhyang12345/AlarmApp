package com.arrata.user.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
import java.util.Calendar;
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

        selectDay(holder.mon, alarm.isMonday());
        selectDay(holder.tue, alarm.isTuesday());
        selectDay(holder.wed, alarm.isWednesday());
        selectDay(holder.thu, alarm.isThursday());
        selectDay(holder.fri, alarm.isFriday());
        selectDay(holder.sat, alarm.isSaturday());
        selectDay(holder.sun, alarm.isSunday());

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            DateFormatSymbols symbols = new DateFormatSymbols(Locale.US);
            sdf.setDateFormatSymbols(symbols);
            final Date dateObj = sdf.parse(hour + ":" + minute);

            SimpleDateFormat next = new SimpleDateFormat("K:mm a");
            next.setDateFormatSymbols(symbols);

            holder.alarmTime.setText(next.format(dateObj).substring(0, next.format(dateObj).length() - 3));
            holder.ampm.setText(next.format(dateObj).substring(next.format(dateObj).length() - 2, next.format(dateObj).length()));
            Log.d("Formatted time", next.format(dateObj));
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
                //getting rid of item in database
                RealmResults<Alarm> results =
                        myRealm.where(Alarm.class)
                                .equalTo("code", code)
                                .findAll();
                myRealm.beginTransaction();
                results.clear();
                myRealm.commitTransaction();

                notifyItemRemoved(position);
                notifyItemRangeChanged(position, alarms.size());

                //getting rid of all possible pendingIntents
                SettingsActivity st = new SettingsActivity();
                int[] gC = st.generateCode(code);
                for(int i = 0; i < 7; ++i) {
                    Log.d("AlarmAdapter", String.valueOf(gC[i]));
                    cancelAlarm(context, gC[i]);
                }

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

                boolean currentState = alarm.isActive();

                myRealm.beginTransaction();
                alarm.setActive(!alarm.isActive());
                myRealm.commitTransaction();

                alarm = myRealm.where(Alarm.class)
                        .equalTo("code", code)
                        .findFirst();

                activate((ImageView) v, alarm.isActive());

                SettingsActivity st = new SettingsActivity();
                int[] gC = st.generateCode(code);
                if(!currentState) {
                    int hours = alarm.getHours();
                    int minutes = alarm.getMinutes();
                    if(alarm.isMonday()) {
                        Intent alarmIntent = new Intent(context, AlarmReceiver.class);

                        alarmIntent.putExtra("code", code);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, gC[Calendar.MONDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        setAlarm(pendingIntent, code, hours, minutes, Calendar.MONDAY);
                    }
                    if(alarm.isTuesday()) {
                        Intent alarmIntent = new Intent(context, AlarmReceiver.class);

                        alarmIntent.putExtra("code", code);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, gC[Calendar.TUESDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        setAlarm(pendingIntent, code, hours, minutes, Calendar.TUESDAY);
                    }
                    if(alarm.isWednesday()) {
                        Intent alarmIntent = new Intent(context, AlarmReceiver.class);

                        alarmIntent.putExtra("code", code);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, gC[Calendar.WEDNESDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        setAlarm(pendingIntent, code, hours, minutes, Calendar.WEDNESDAY);
                    }
                    if(alarm.isThursday()) {
                        Intent alarmIntent = new Intent(context, AlarmReceiver.class);

                        alarmIntent.putExtra("code", code);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, gC[Calendar.THURSDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        setAlarm(pendingIntent, code, hours, minutes, Calendar.THURSDAY);
                    }
                    if(alarm.isFriday()) {
                        Intent alarmIntent = new Intent(context, AlarmReceiver.class);

                        alarmIntent.putExtra("code", code);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, gC[Calendar.FRIDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        setAlarm(pendingIntent, code, hours, minutes, Calendar.FRIDAY);
                    }
                    if(alarm.isSaturday()) {
                        Intent alarmIntent = new Intent(context, AlarmReceiver.class);

                        alarmIntent.putExtra("code", code);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, gC[Calendar.SATURDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        setAlarm(pendingIntent, code, hours, minutes, Calendar.SATURDAY);
                    }
                    if(alarm.isSunday()) {
                        Intent alarmIntent = new Intent(context, AlarmReceiver.class);

                        alarmIntent.putExtra("code", code);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, gC[Calendar.SUNDAY], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        setAlarm(pendingIntent, code, hours, minutes, Calendar.SUNDAY);
                    }

                } else {
                    for(int i = 0; i < 7; ++i) {
                        Log.d("AlarmAdapter", String.valueOf(gC[i]));
                        cancelAlarm(context, gC[i]);
                    }
                }

            }
        });



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

    void cancelAlarm(Context context, int alarmId) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
    }

    void setAlarm(PendingIntent pendingIntent, int code, int hours, int minutes, int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar current = Calendar.getInstance();
        current.getTimeInMillis();

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

    void selectDay(TextView v, boolean value) {
        if(value) {
            v.setBackgroundResource(R.drawable.selected_background);
            v.setTextColor(Color.WHITE);
        } else {
            v.setBackgroundResource(0);
            v.setTextColor(Color.BLACK);
        }
    }

    void activate(ImageView v, boolean active) {
        if(active) {
            v.setAlpha((float) 1.0);
        } else {
            v.setAlpha((float) 0.1);
        }
    }
}
