/* This creates A log message and toasts whenever MainActivity is:
*  created, resumed, paused, stopped, or destroyed.
*  It's an attempt to show an android apps' activity cycle while using the app.
*
*  This does not demonstrate saveing or loading states.
* View logcat to get realtime info when activity state changes. This is the only way.
*/

package com.crazyj36.activitycycle;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;

public class MainActivity extends Activity {

    // A constant reference name to be used to find this apps log in your devices logcat messages.
    private static final String appLogTag = "ACTIVITYCYCLELOGTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	   	setContentView(R.layout.activity_main);

        String msg = "onCreate()";
        Log.i(appLogTag, msg);
        toastStatus(msg);
    }

    @Override
    protected void onPause() {
        // 'Super' as in MainActivitys' onPause() (activity put in background)
        super.onPause();
        String msg = "onPause()";
        Log.i(appLogTag, msg);
        toastStatus(msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String msg = "onResume()";
        Log.i(appLogTag, msg);
        toastStatus(msg);

    }

    @Override
    protected void onStop() {
        super.onStop();
        String msg = "onStop()";
        Log.i(appLogTag, msg);
        toastStatus(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String msg = "onDestroy()";
        Log.i(appLogTag, msg);
        toastStatus(msg);
    }

    public void toastStatus(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

}
