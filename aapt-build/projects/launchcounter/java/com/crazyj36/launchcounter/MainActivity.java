package com.crazyj36.launchcounter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static int count;
    String countString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countString = "Launch count = " + String.valueOf(count);
        Toast.makeText(MainActivity.this, countString, Toast.LENGTH_SHORT).show();

        /* Calling finish() rather immediately prevents app from needing to go into recents. */
        // this.finish();
    }

    @Override
    public void onDestroy() {
        count = count + 1;
        super.onDestroy();
    }

}
