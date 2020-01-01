package com.crazyj36.timercount;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        x = x + 1;
                        Toast.makeText(MainActivity.this, String.valueOf(x), Toast.LENGTH_SHORT).show();
                        if (x == 5) {
                            timer.cancel();
                            Toast.makeText(MainActivity.this, "Timer Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            );
        }
        }, 0 , 2000);
    }

}
