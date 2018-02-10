package com.example.vip.abdr_app_a_d;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationReciever extends Service {
    Context c = null;
    String s;
    double latitude, longitude;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 5000; // in Milliseconds
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    String [] ar=null;
    private static final String LOG_TAG = "NotificationReciever";

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.c = getApplicationContext();
        inertnetcheck in = new inertnetcheck();
        if (in.chkStatus(c)) {
        if (new localuser(c).getIfLoggedInUser()) {

            if (new localuser(c).getLoggedInUser().flag.equals("d")) {


                Timer timer = new Timer();


                TimerTask hourlyTask = new TimerTask() {

                    @Override
                    public void run() {
                        checkCB b = new checkCB();
                        b.execute();

                    }

                };

                timer.schedule(hourlyTask, 0l, 10000 * 60 * 60);

                if (new localuser(c).getcbooking() != null) {

                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    Log.d("RTGPSLocationService", " " + isGPSEnabled + " " + isNetworkEnabled);
                    if (ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {
                    } else {
                        if (isGPSEnabled) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                    MINIMUM_TIME_BETWEEN_UPDATES,
                                    MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
                            Location location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        } else if (isNetworkEnabled) {
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                    MINIMUM_TIME_BETWEEN_UPDATES,
                                    MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
                            Location location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        }


                    }
                }

                Timer timer1 = new Timer();
                TimerTask hourlyTask1 = new TimerTask() {

                    @Override
                    public void run() {

                            BackGround1 b = new BackGround1();
                            b.execute();

                    }
                };

                timer1.schedule(hourlyTask1, 0l, 15000 * 60 * 60);
            }
        }
        else if (new localuser(c).getLoggedInUser().flag.equals("a")) {
            Timer timer1 = new Timer();
            TimerTask hourlyTask1 = new TimerTask() {

                @Override
                public void run() {
                    aBackGround b = new aBackGround();
                    b.execute();

                }
            };

            timer1.schedule(hourlyTask1, 0l, 15000 * 60 * 60);
        }
        }
            return START_STICKY;
        }



    private class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            String message = String.format("New Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude());
            longitude=location.getLongitude();
            latitude=location.getLatitude();
           // Log.d(TAG, location.getLatitude() + " " + location.getLongitude());
            //Log.d(TAG, message);
            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                public void run() {
                    inertnetcheck in=new inertnetcheck();
                    if(in.chkStatus(c)) {
                        BackGround b = new BackGround();
                        b.execute(longitude, latitude);
                    }
                }
            });



        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }


    }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.i(LOG_TAG, "In onDestroy");
        }

        @Override
        public IBinder onBind(Intent intent) {
            // Used only in case of bound services.
            return null;
        }
    class BackGround1 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";
            int tmp;

            try {//192.168.56.1
                URL url = new URL("http://abdr.000webhostapp.com/drivernoti.php");
                String urlParams = "sid=" + new localuser(c).getsid()+"&did="+new localuser(c).getLoggedInUser().ID;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {

            //Toast.makeText(c, s, Toast.LENGTH_LONG).show();
            if (s.equals("false")) {
                Toast.makeText(c, "not found", Toast.LENGTH_LONG).show();
            } else {
                JSONObject root = null;
                try {
                    root = new JSONObject(s);
                    JSONArray array = root.getJSONArray("user_data");
                    if (root.has("user_data")) {
                        if (array.length()>0) {
                            new localuser(c).setsid(array.getJSONObject(0).getInt("service_id"));
                            Intent intent1 = new Intent(c, approvebooking.class);
                            intent1.putExtra("service_id", array.getJSONObject(0).getInt("service_id"));
                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            PendingIntent pendingIntent = PendingIntent.getActivity(c, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                            NotificationManager notificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(c).
                                    setSmallIcon(R.drawable.logo).
                                    setContentIntent(pendingIntent).
                                    setContentText(" New booking click to approve it or decline it ").
                                    setContentTitle("New booking").
                                    setAutoCancel(true)
                                    .setSound(alarmSound);
                            notificationManager.notify(100, builder.build());
                        }
                    }
                } catch (JSONException e) {
                   // Toast.makeText(c, "service#1"+e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }


        }
    }
    class BackGround extends AsyncTask<Object, String, String> {
        double longa;
        double lat;
        @Override
        protected String doInBackground(Object... params) {
             longa=(double)params[0];
             lat=(double)params[1];

            String data = "";
            int tmp;

            try {//192.168.56.1
                URL url = new URL("http://abdr.000webhostapp.com/currentbookingdriverupdate.php");
                String urlParams = "long="+longa+"&lat="+lat+"&D_ID="+new localuser(c).getLoggedInUser().ID;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {

            if (s.equals("false")) {
                Toast.makeText(c, "service#2"+"not found", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(c, s+longa+lat, Toast.LENGTH_LONG).show();

            }


        }
    }

    class checkCB extends AsyncTask<Object, String, String> {
        @Override
        protected String doInBackground(Object... params) {


            String data = "";
            int tmp;

            try {//192.168.56.1
                URL url = new URL("http://abdr.000webhostapp.com/currentbookingdriver.php");
                String urlParams = "D_ID="+new localuser(c).getLoggedInUser().ID;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {

            if (s.equals("false")) {
                Toast.makeText(c, "service#3"+"not found", Toast.LENGTH_LONG).show();
            } else {

                JSONObject  root ;
                try {
                    root = new JSONObject(s);
                    if(root.has("user_data")) {
                        JSONArray array = root.getJSONArray("user_data");
                        if (array.length() > 0 && array.getJSONObject(0).getString("status").equals("Pickedup")) {
                            new localuser(c).setcbooking(array.getJSONObject(0).getString("date") + "," + array.getJSONObject(0).getString("time"));
                        } else {
                            new localuser(c).setcbooking(null);
                        }
                    }
                } catch (JSONException e) {
                    //Toast.makeText(c, "service#3"+e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }


        }
    }
    class aBackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";
            int tmp;

            try {//192.168.56.1
                URL url = new URL("http://abdr.000webhostapp.com/adminnoti.php");
                String urlParams = "c_id="+new localuser(c).getsuid();

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("false")) {
                Toast.makeText(c, "not found", Toast.LENGTH_LONG).show();
            } else {

                JSONObject root = null;
                try {
                    root = new JSONObject(s);
                    JSONArray array = root.getJSONArray("user_data");

                    if (root.has("user_data")) {
                        if (array.length() > 0) {
                            new localuser(c).setsuid(array.getJSONObject(0).getInt("complain_id"));

                            Intent intent1 = new Intent(c, Show_support.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            PendingIntent pendingIntent = PendingIntent.getActivity(c, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                            NotificationManager notificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(c).
                                    setSmallIcon(R.drawable.logo).
                                    setContentIntent(pendingIntent).
                                    setContentText(" New support case  click to answer it ").
                                    setContentTitle("New support case").
                                    setAutoCancel(true)
                                    .setSound(alarmSound);
                            notificationManager.notify(100, builder.build());
                        }
                    }
                } catch (JSONException e) {
                   // Toast.makeText(c, "service#4"+e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }


        }
    }

    }