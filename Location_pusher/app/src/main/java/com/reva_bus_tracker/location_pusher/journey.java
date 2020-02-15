package com.reva_bus_tracker.location_pusher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class journey extends AppCompatActivity {
    Button strt,stp;
    private LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //that hour of the day concept comes here
        setContentView(R.layout.activity_journey);
        stp = findViewById(R.id.button3);
        strt = findViewById(R.id.button2);

        strt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check_for_internet()){
                    diagg();
                }
                else if(!check_for_location())
                {
                    showAlert();
                }
                else
                {
                    startService(new Intent(journey.this,MyService.class));
                    Toast.makeText(getApplicationContext(),"starting service",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        stp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(journey.this,MyService.class));
                Toast.makeText(getApplicationContext(),"stopping service",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean check_for_location() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void diagg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Network Error").setMessage("It seems that you have turned off your internet connection please turn on in or");
        builder.setPositiveButton("0k", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        builder.show();
    }

    private void sta() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName("com.android.phone","com.android.phone.NetworkSetting");
        startActivity(intent);
    }

    private boolean check_for_internet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
