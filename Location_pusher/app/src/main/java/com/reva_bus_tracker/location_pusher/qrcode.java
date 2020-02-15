package com.reva_bus_tracker.location_pusher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class qrcode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setBeepEnabled(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);  // optional
        integrator.setOrientationLocked(true);// allow barcode scanner in potrait mode
        integrator.initiateScan();


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String value = result.getContents();
            if (value != null) {
                Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();
                // do something
                if (cons.parsee(value)){
                    Toast.makeText(getApplicationContext(),"\n"+cons.route_number,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(qrcode.this,journey.class));
                    finish();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Error !!!!");
                    builder.setMessage("Error in parsing the qr code please restart the app");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();
                }
            } else {
                Toast.makeText(getApplicationContext(),"QR Code scan failed",Toast.LENGTH_SHORT).show();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
