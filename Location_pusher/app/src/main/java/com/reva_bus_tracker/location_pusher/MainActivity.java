package com.reva_bus_tracker.location_pusher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPer();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(cons.preff_name,MODE_PRIVATE);
        final String value = sharedPreferences.getString("login-data","");

        if(!check_for_internet()){
            diagg();
        }else{
            imageView = findViewById(R.id.imageView);
            final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.animm);
            imageView.startAnimation(animation);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    methode(value);
                }
            },4000);
        }

    }

    private void methode(final String value) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("message/passcode");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fb_value = dataSnapshot.getValue(String.class);
                cons.vald = fb_value;
                if (fb_value.equals(value)){
                    //here we will start the qr code activity
                    startActivity(new Intent(MainActivity.this,qrcode.class));
                    finish();
                }else{
                    //here we will start pin activity
                    startActivity(new Intent(MainActivity.this,Pin.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    private boolean check_for_internet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void requestPer() {
        Dexter.withActivity(this).withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                //if there are any
                if (report.isAnyPermissionPermanentlyDenied()){
                    showd();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {

            }
        }).onSameThread().check();
    }

    private void showd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions are required please grant permission").setMessage("This app needs permission to update location please Grant permissions");
        builder.setPositiveButton("GOTO SETTING", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        builder.show();
    }
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("parts",getPackageName(),null);
        intent.setData(uri);
        startActivityForResult(intent,101);
    }
}
