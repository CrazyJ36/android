package com.crazyj36.wait;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

        Toast.makeText(this, "one", Toast.LENGTH_SHORT).show();
        myRunnable();
    }

    public void myRunnable() {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "two", Toast.LENGTH_SHORT).show();
            }
        }, 5000);

    }
}
