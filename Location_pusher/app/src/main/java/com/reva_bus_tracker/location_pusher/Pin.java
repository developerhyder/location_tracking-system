package com.reva_bus_tracker.location_pusher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Pin extends AppCompatActivity {

    EditText ed;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        ed = findViewById(R.id.editText);
        button = findViewById(R.id.button);

        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(cons.preff_name,MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        //TODO: integrate firebase
        //TODO: Use hash value
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verify_code(ed.getText().toString())){

                    editor.putString("login-data",ed.getText().toString());
                    editor.commit();

                    Toast.makeText(getApplicationContext(),"Verified",Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(),"Wrong pin",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean verify_code(final String toString) {
        if (cons.vald.equals(toString)){
            return true;
        }
        else{
            return false;
        }
    }

}
