package com.REVABusTracker.student_side;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

public class update extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Network Error").setMessage("It seems that you have turned off your internet connection please turn on in or");
        builder.setPositiveButton("0k", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
    }
}
