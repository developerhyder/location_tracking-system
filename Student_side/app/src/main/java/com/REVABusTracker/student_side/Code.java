package com.REVABusTracker.student_side;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Code extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button b1;
    EditText ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        b1 = findViewById(R.id.button);
        ed = findViewById(R.id.editText);
        if (!check_for_internet()){
            diagg();
        }
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("message/update");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean value = dataSnapshot.getValue(boolean.class);
                if (value){
                    Toast.makeText(getApplicationContext(),"Update available",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Code.this,update.class));
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String edt = ed.getText().toString().trim();

                if (var_meth(edt)){
                    cons.route_no = edt;
                    startActivity(new Intent(Code.this,mapss.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Please enter valid route number",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean var_meth(String edt) {
        int k = Integer.parseInt(edt);
        if (k>0 && k<=30){
            return true;
        }
        return false;
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private boolean check_for_internet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.code, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //query
            startActivity(new Intent(Code.this,query.class));
        } else if (id == R.id.nav_gallery) {
            //trouble shoot
            startActivity(new Intent(Code.this,troubleShoot.class));
        } else if (id == R.id.nav_slideshow) {
            //About The APP
            startActivity(new Intent(Code.this,About_app.class));
        } else if (id == R.id.nav_tools) {
            //About Us
            startActivity(new Intent(Code.this,About_Us.class));

        } else if (id == R.id.nav_share) {
            //share
            String message = "Missed the bus Again :(    No problemo REVA_Bus_Tracker is here to rescue - An app which can track your route number and the fun part is ! It's developed by student body from scratch!!! Download now\n\n";
            message=message+"https://play.google.com/store/apps/details?id=";
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, message+getPackageName());
            startActivity(Intent.createChooser(share, "Share the app with your friends"));
        } else if (id == R.id.nav_send) {
            //settings
            System.exit(0);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
