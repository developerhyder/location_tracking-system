package com.reva_bus_tracker.location_pusher;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobScheduler;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MyService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<Status> {

    private static final int ID_SERVICE = 101;

    Calendar cal;

    /**
     * Update interval of location request
     */
    private final int UPDATE_INTERVAL = 1000;

    /**
     * fastest possible interval of location request
     */
    private final int FASTEST_INTERVAL = 900;


    /**
     * The Tag.
     */
    String TAG = "MyService";

    /**
     * LocationRequest instance
     */
    private LocationRequest locationRequest;

    /**
     * GoogleApiClient instance
     */
    private GoogleApiClient googleApiClient;

    /**
     * Location instance
     */
    private Location lastLocation;

    /**
     * Method is called when location is changed
     * @param location - location from fused location provider
     */

    @Override
    public void onLocationChanged(Location location) {

        Log.d(TAG, "onLocationChanged [" + location + "]");
        lastLocation = location;
        writeActualLocation(location);
    }
    @SuppressLint("MissingPermission")
    private void getLastKnownLocation() {
        //Log.d(TAG, "getLastKnownLocation()");
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            Log.i(TAG, "LasKnown location. " +
                    "Long: " + lastLocation.getLongitude() +
                    " | Lat: " + lastLocation.getLatitude());
            writeLastLocation();
            startLocationUpdates();

        } else {
            Log.w(TAG, "No location retrieved yet");
            startLocationUpdates();

            //here we can show Alert to start location
        }
    }
    @SuppressLint("SetTextI18n")
    private void writeActualLocation(Location location) {
        Log.d(TAG, "actual:"+location.getLatitude() + ", " + location.getLongitude());
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("message/route/"+cons.route_number+"/location");
        reference.setValue(location.getLatitude()+","+location.getLongitude());
    }

    /**
     * this method only provokes writeActualLocation().
     */
    private void writeLastLocation() {
        writeActualLocation(lastLocation);
    }
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Override
    public void onCreate() {
        FirebaseApp.initializeApp(this);
        super.onCreate();
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    stopSelf();
                }
                case 2:break;
            }
        }
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        cal = Calendar.getInstance();

        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);




        //do heavy work on a background thread


        createGoogleApi();
        //stopSelf();
        handler.sendEmptyMessageDelayed(1,120 * 60000);

        return START_NOT_STICKY;
    }
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }



    /**
     * Create google api instance
     */
    private void createGoogleApi() {
        //Log.d(TAG, "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        //connect google api
        googleApiClient.connect();

    }

    @Override
    public void onDestroy(){
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("message/route/"+cons.route_number+"/location");
        reference.setValue("13.115600,77.635153");
        super.onDestroy();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Log.i(TAG, "onConnected()");
        getLastKnownLocation();
    }

    /**
     * this method returns whether connection is suspended
     * @param i - 0/1
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG,"connection suspended");
    }

    /**
     * this method checks connection status
     * @param connectionResult - connected or failed
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"connection failed");
    }

    /**
     * this method tells the result of status of google api client
     * @param status - success or failure
     */
    @Override
    public void onResult(@NonNull Status status) {
        Log.d(TAG,"result of google api client : " + status);
    }

}
