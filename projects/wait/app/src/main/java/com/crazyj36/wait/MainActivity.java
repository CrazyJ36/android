package com.crazyj36.wait;
// This is not so much A loop as it does two things.
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // print hello
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        // print world 5 seconds later
        myRunnable("World");
    }

    public void myRunnable(final String text) {
        Handler handler = new Handler();
        // postDelayed make run() code run at a later time.
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        }, 5000);
    }
}
