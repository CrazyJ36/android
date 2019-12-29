/* 'i' in Log.i sends an INFO log message to devices logger, viewable by logcat.
*  besides 'i', there are: Log.d (for debug), Log.e (error), Log.v (verbose), Log.w (warning),
* and Log.wtf() in extreme cases.
*
* function definition for common usage:
* Log.i(String appReferenceLogTag, String terminalMessage);
*
* Also, you can use println(int priority, String tag, String message) from normal java cmd line to produce logs.
*
* View logcat to get realtime info when activity state changes. This is the only way.
*/

package com.crazyj36.activitycycle;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;

public class MainActivity extends Activity {

    String saveData;

    // A constant reference name to be used to find this apps log in your devices logcat messages.
    private static final String appLogTag = "ACTIVITYCYCLELOGTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	   	setContentView(R.layout.activity_main);

        String msg = "activity created";
        Log.i(appLogTag, msg);
        toastStatus(msg);
    }

    @Override
    protected void onPause() {
        // 'Super' as in MainActivitys' onPause() (activity put in background)
        super.onPause();
        String msg = "activity paused";
        Log.i(appLogTag, msg);
        toastStatus(msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String msg = "activity resumed";
        Log.i(appLogTag, msg);
        toastStatus(msg);
    }

    @Override
    protected void onStop() {
        super.onStop();
        String msg = "activity stopped";
        Log.i(appLogTag, msg);
        toastStatus(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String msg = "fully exited and destroyed";
        Log.i(appLogTag, msg);
        toastStatus(msg);
    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        String msg = "state saved app with bundle";
        savedInstanceState.putString("save data", saveData);
        Log.i(appLogTag, msg);
        toastStatus(msg);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String msg = "loaded savedInstance";
        Log.i(appLogTag, msg + "loaded: " + savedInstanceState.getString(saveData));
        toastStatus(msg);
    }

    public void toastStatus(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

}
