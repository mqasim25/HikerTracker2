package com.example.hikertracker;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


public class GoogleService extends Service implements LocationListener {

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;

    double latitude, longitude;
    Location location;
    LocationManager locationManager;


    String stringFromFragment;
    Boolean trueFalseValue;

    private Handler mHandler = new Handler();
    Intent intent1;

    //long notify_interval = 10000;
    public static String str_receiver = "servicetutorial.service.receiver";

    public static Timer mTimer;

    long val;

    int value;

    public GoogleService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {



        /*Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, "MyChannel_ID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Hike Tracker")
                .setContentText("Tracking Location...")
                .setContentIntent(pendingIntent).build();

        startForeground(1337, notification);*/

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            startForeground();
//        }

        startForeground();


        //Log.e("ValueOnCreate", stringFromFragment);


        //Log.e("StringValue",stringFromFragment +"");

        /*locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                fn_update(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

                //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(intent);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, val, 0, locationListener);*/




    }


    /*private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String str = intent.getStringExtra("Time");

            Log.e("TimeFound", str);


        }
    };*/

    /*@Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }*/


    /*@Override
    public void onLocationChanged(Location location) {

        fn_update(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }*/

    @Override
    public void onLocationChanged(Location location) {

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


    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            /*if (trueFalseValue) {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        fn_getlocation();

                        //Log.e("Value", stringFromFragment);
                    }
                });
            } else {*/

            mHandler.post(new Runnable() {
                @Override
                public void run() {


                    try {

                        fn_getlocation();

                        Log.e("Value", stringFromFragment);

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                    }
                }
            });
            //}
        }
    }

    private void fn_getlocation() {



        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {

        } else {

            if (isNetworkEnable) {
                location = null;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, val, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        Log.e("latitudeNetwork", location.getLatitude() + "");
                        Log.e("longitudeNetwork", location.getLongitude() + "");

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update(location);
                    }
                }

                //assert locationManager != null;
                //locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);

            }


            if (isGPSEnable) {
                location = null;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, val, 0, this);
                if (locationManager!=null){
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location!=null){
                        Log.e("latitudeGPS",location.getLatitude()+"");
                        Log.e("longitudeGPS",location.getLongitude()+"");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        //fn_update(location);
                    }
                }
            }


        }



//
//
//
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//
//
//        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, val, 0, locationListener);
//
//
//
//
//        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
//        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//        /*if (trueFalseValue) {
//
//            if (!isGPSEnable && !isNetworkEnable) {
//
//            } else {
//
//                if (isNetworkEnable) {
//                    location = null;
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return;
//                    }
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//                    if (locationManager!=null){
//                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        if (location!=null){
//
//                            Log.e("latitude",location.getLatitude()+"");
//                            Log.e("longitude",location.getLongitude()+"");
//
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                            fn_update(location);
//                        }
//                    }
//
//                }
//
//                if (isGPSEnable){
//                    location = null;
//                    assert locationManager != null;
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,locationListener);
//                    if (locationManager!=null){
//                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                        if (location!=null){
//                            Log.e("latitude",location.getLatitude()+"");
//                            Log.e("longitude",location.getLongitude()+"");
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                            fn_update(location);
//                        }
//                    }
//                }
//
//
//            }
//        } else {*/
//
//        if (!isGPSEnable && !isNetworkEnable) {
//
//        } else {
//
//                if (isNetworkEnable) {
//                    location = null;
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return;
//                    }
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, val, 0, locationListener);
//                    if (locationManager!=null){
//                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        if (location!=null){
//
//                            Log.e("latitude1",location.getLatitude()+"");
//                            Log.e("longitude1",location.getLongitude()+"");
//
//                            //latitude = location.getLatitude();
//                            //longitude = location.getLongitude();
//                            //fn_update(location);
//                        }
//                    }
//
//                }
//
//            if (isGPSEnable) {
//                location = null;
//                assert locationManager != null;
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, val, 0, locationListener);
//                    if (locationManager!=null){
//                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                        if (location!=null){
//                            Log.e("latitude2",location.getLatitude()+"");
//                            Log.e("longitude2",location.getLongitude()+"");
//                            //latitude = location.getLatitude();
//                            //longitude = location.getLongitude();
//                            //fn_update(location);
//                        }
//                    }
//                }
//
//
//            }
//        //}



    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);

        //Intent myIntent = getIntent(); // this getter is just for example purpose, can differ

        /*if (Objects.equals(intent.getAction(), "com.example.locationaccess")) {
            emailStr = intent.getStringExtra("Email");
            switchTFStr = Boolean.valueOf(intent.getStringExtra("SwitchKey"));
        } else {
            Log.d("Not received", "Received intent with action="+ intent.getAction()+"; now what?");
        }*/

        //Objects.requireNonNull(getApplicationContext()).registerReceiver(broadcastReceiver, new IntentFilter(HomeFragment.str_rec));



        trueFalseValue = intent.getBooleanExtra("BoolTrue", true);

        if (trueFalseValue) {

            //Timer mTimer = new Timer();
            //mTimer.scheduleAtFixedRate(new TimerTaskToGetLocation(), 0, 500000);
            //intent = new Intent(str_receiver);

            stringFromFragment = intent.getStringExtra("Time");
            //value = Integer.valueOf(stringFromFragment);
            //Log.e("String",value +"");

            //value = Integer.parseInt(stringFromFragment);
            //Log.e("StringValue",value +"");

            //Log.e("OnStartCommanTrue", stringFromFragment);
            //Log.e("onStartCommandTrue", "Onstartcommand");

            value = Integer.parseInt(stringFromFragment);
            //Log.e("ValueOnStart", value);

            val = (long) (value * 60000);

            mTimer = new Timer();
            mTimer.schedule(new TimerTaskToGetLocation(), 0, val);
            intent1 = new Intent(str_receiver);

        } else {

            stringFromFragment = intent.getStringExtra("Time");
            //value = Integer.valueOf(stringFromFragment);
            //Log.e("String",value +"");

            //value = Integer.parseInt(stringFromFragment);
            //Log.e("StringValue",value +"");

            //Log.e("OnStartCommanFalse", stringFromFragment);
            //Log.e("onStartCommandFalse", "Onstartcommand");

            value = Integer.parseInt(stringFromFragment);



            //Log.e("ValueOnStart", value);

            //3600000
            val = (long) (value * 3600000);

            mTimer = new Timer();

            //mTimer.cancel();

            mTimer.schedule(new TimerTaskToGetLocation(), 0, val);
            intent1 = new Intent(str_receiver);
        }


        // TODO do something with the string

        return START_NOT_STICKY;
    }

    private void fn_update(Location location){

        Log.e("Receiving LatLon", "Receive");

        intent1.putExtra("latitude",location.getLatitude()+"");
        intent1.putExtra("longitude",location.getLongitude()+"");
        sendBroadcast(intent1);

        /*database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Users").child("Email");

        Email email = new Email(emailStr);
        reference.setValue(email);

        reference = database.getReference().child("Users").child("Email").child("Locations").push();

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        millisInString = dateFormat.format(new Date());

        Locations locations = new Locations(latitude, longitude, millisInString, switchTFStr, "A");

        reference.setValue(locations);

        reference = database.getReference().child("Users").child("Email").child("UDID");
        //uniqueID = UUID.randomUUID().toString();
        uniqueID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        //tv_uniqueIdentifier.setText(uniqueID);
        reference.setValue(uniqueID);*/
    }

    private void startForeground() {

        //val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        String channelId = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel();
        } else {
            // If earlier version channel ID is not used
            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, channelId)
                //.setSmallIcon(R.drawable.icon)
                .setContentTitle("Hike Tracker")
                .setContentText("Tracking Location...")
                .setContentIntent(pendingIntent).build();

        startForeground(1337, notification);
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel() {

        String channelId = "my_service";
        String channelName = "My Background Service";
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.setImportance(NotificationManager.IMPORTANCE_NONE);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(notificationChannel);

        return channelId;
    }


    /*@Override
    public void onDestroy() {
        super.onDestroy();

        //Objects.requireNonNull(getApplicationContext()).unregisterReceiver(broadcastReceiver);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            stopService(intent1);
            stopSelf();
            stopForeground(true);
        }

        //mTimer.cancel();

    }*/
}



