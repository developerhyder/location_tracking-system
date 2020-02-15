package com.REVABusTracker.student_side;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class About_app extends AppCompatActivity {
    private int progressStatus = 0;
    private int count = 0;
    private Handler handler = new Handler();
    ScrollView scrollView;
    private int i=0;
    MediaPlayer mp;
    TextView tv;
    String test = "This app is designed to ease life of the students who suffer during rush hours and many times They miss the bus as a result they have to find a alternate source to commute and we took this as a problem to solve and came up with this solution ! pretty simple but quite effective\n\n\nOver and out ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        tv = (TextView)findViewById(R.id.textView0);
        scrollView = findViewById(R.id.sv);
        mp = MediaPlayer.create(this, R.raw.k4);
        somee(mp);
    }
    private void somee(final MediaPlayer mp) {
        final int ct = test.length();
        final char[] ch = test.toCharArray();

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < ct) {
                    progressStatus += 1;
                    handler.post(new Runnable() {
                        public void run() {
                            mp.start();
                            String s = Character.toString(ch[i]);
                            scrollView.fullScroll(View.FOCUS_DOWN);

                            tv.append(s);
                            s="";
                            i++;
                            if (i == ct-1){
                                mp.stop();
                            }
                        }
                    });
                    try {
                        // Sleep for 600 milliseconds.
                        Thread.sleep(75);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus){
            mp.stop();
        }
        super.onWindowFocusChanged(hasFocus);
    }
}
