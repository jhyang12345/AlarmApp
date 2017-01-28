package com.arrata.user.alarmapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

public class AlarmActive extends AppCompatActivity {

    AudioManager audioManager;
    static int originalVolume;

    Button stopButton;

    TextView alarmMessage;

    TextView alarmTime;
    TextView alarmAMPM;

    Alarm alarm;

    LocationManager locationManager;
    LocationListener oneShotLocationListener;
    Double lat, lon;

    static String address;

    static int code;
    static int hours;
    static int minutes;
    static String message;

    AlarmManager alarmManager;

    Vibrator vibrator;

    Realm myRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_active);

        alarmMessage = (TextView) findViewById(R.id.alarmMessage);
        alarmTime = (TextView) findViewById(R.id.alarmTime);
        alarmAMPM = (TextView) findViewById(R.id.alarmAmPm);

        AudioPlayer.stop();
        AudioPlayer.play(this, R.raw.lovely);

        myRealm = Realm.getInstance(this);

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

        initializeAlarm();

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        int[] gC = new SettingsActivity().generateCode(code);
        Calendar current = Calendar.getInstance();

        Intent alarmIntent = new Intent(AlarmActive.this, AlarmReceiver.class);

        alarmIntent.putExtra("code", code);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmActive.this, gC[current.get(Calendar.DAY_OF_WEEK)], alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        setAlarm(pendingIntent, code, hours, minutes, current.get(Calendar.DAY_OF_WEEK));


        alarmMessage.setText(message);
        if(message.equals("")) alarmMessage.setText("알람");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            DateFormatSymbols symbols = new DateFormatSymbols(Locale.US);
            sdf.setDateFormatSymbols(symbols);
            final Date dateObj = sdf.parse(String.valueOf(hours) + ":" + String.valueOf(minutes));

            SimpleDateFormat next = new SimpleDateFormat("K:mm a");
            next.setDateFormatSymbols(symbols);

            alarmTime.setText(next.format(dateObj).substring(0, next.format(dateObj).length() - 3));
            alarmAMPM.setText(next.format(dateObj).substring(next.format(dateObj).length() - 2, next.format(dateObj).length()));

        } catch (ParseException e) {

            alarmTime.setText("");
            alarmAMPM.setText("");
            e.printStackTrace();
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        oneShotLocationListener = new OneShotLocation();
        try {
            if(isNetworkConnected()) {

                ActivityCompat.requestPermissions(this, new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION },
                        1);

            } else {
                Log.d("Network unconnected", "Network not connected!");
            }
        } catch(SecurityException e) {
            e.printStackTrace();
            System.out.println("Failed to receive location");
        }



    }


    @Override
    protected void onDestroy() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
        stopAlarm();
        try {
            locationManager.removeUpdates(oneShotLocationListener);
        } catch(SecurityException e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    void stopAlarm() {
        vibrator.cancel();
        AudioPlayer.stop();
        try {
            locationManager.removeUpdates(oneShotLocationListener);
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    void initializeAlarm() {
        Intent intent = getIntent();
        code = intent.getIntExtra("code", 0);
        alarm = myRealm.where(Alarm.class)
                .equalTo("code", code)
                .findFirst();
        if(alarm == null || code == 0) {
            alarm = myRealm.where(Alarm.class)
                    .findFirst();
        }
        hours = alarm.getHours();
        minutes = alarm.getMinutes();
        message = alarm.getMessage();
    }

    void setAlarm(PendingIntent pendingIntent, int code, int hours, int minutes, int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        Calendar current = Calendar.getInstance();
        current.getTimeInMillis();

        //Setting the next alarm!!!
        long trigger = 0 ;
        trigger = calendar.getTimeInMillis();
        while(trigger <= current.getTimeInMillis()) {
            trigger += AlarmManager.INTERVAL_DAY * 7;
        }

        System.out.println("Trigger from active alarm " + trigger + " " + current.getTimeInMillis());

        if(Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
            //    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000,pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
            //    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000,pendingIntent);
        }
        Log.d("setAlarm", "alarm set at " + calendar.toString());

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    class OneShotLocation implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            Log.d("LocationChanged", "Location changed!");
            Toast.makeText(AlarmActive.this, "Latitude: " + lat + " " + "Longitude: " + lon, Toast.LENGTH_LONG).show();
            new requestAddressTask().execute();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("Provider unprovided!", "");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                System.out.println("Succesfully received permissions");
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 1, oneShotLocationListener);
                        //locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, oneShotLocationListener, null);
                        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        //Log.d("Location lat", String.valueOf(location.getLatitude()));
                        //Log.d("Location lon", String.valueOf(location.getLongitude()));
                        if(location != null) {
                            location.getLatitude();
                        }
                        if(lat == null) {
                            Toast.makeText(AlarmActive.this, "GPS or Network inaccessible", Toast.LENGTH_LONG).show();
                        }
                        System.out.println(lat + " " + lon);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }


                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static String getAddressFromCoordinate(Context context, double lat, double lon) {
        String ret = "No address found!";
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        List<Address> addresses;
        try {
            if(geocoder != null) {
                addresses = geocoder.getFromLocation(lat, lon, 1);
                if(addresses != null && addresses.size() > 0) {
                    ret = addresses.get(0).getAddressLine(0).toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private class requestAddressTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... vars) {
            address = getAddressFromCoordinate(AlarmActive.this, lat, lon);

            return null;
        }

        protected void onProgressUpdate(Void... progress) {

        }

        protected void onPostExecute(Void result) {
            System.out.println("Something done");
            Toast.makeText(AlarmActive.this, address, Toast.LENGTH_LONG).show();
        }
    }
}

