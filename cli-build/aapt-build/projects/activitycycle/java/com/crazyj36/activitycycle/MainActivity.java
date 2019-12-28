/* 'i' in Log.i sends an INFO log message to devices logger, viewable by logcat.
*  besides 'i', there are: Log.d (for debug), Log.e (error), Log.v (verbose), Log.w (warning),
* and Log.wtf() in extreme cases.
*
* function definition for common usage:
* Log.i(String appReferenceLogTag, String terminalMessage);
*
* Also, you can use println(int priority, String tag, String message) from normal java cmd line to produce logs.
*/

package com.crazyj36.activitycycle;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;

public class MainActivity extends Activity {

    // A constant reference name to be used to find this apps log in your devices logcat messages.
    private static final String appLogTag = "APIPLAYGROUNDANYSDKLOGTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	   	setContentView(R.layout.activity_main);

        String msg = "apiplayground created";
        Log.i(appLogTag, msg);
        toastStatus(msg);
    }

    @Override
    protected void onPause() {
        // 'Super' as in MainActivitys' onPause() (activity put in background)
        super.onPause();
        String msg = "apiplayground paused";
        Log.i(appLogTag, msg);
        toastStatus(msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String msg = "apiplayground resumed";
        Log.i(appLogTag, msg);
        toastStatus(msg);
    }

    @Override
    protected void onStop() {
        super.onStop();
        String msg = "apiplayground stopped";
        Log.i(appLogTag, msg);
        toastStatus(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String msg = "apiplayground fully exited and destroyed";
        Log.i(appLogTag, msg);
        toastStatus(msg);
    }

    @Override
    protected void onRestoreInstanceState(Bundle load) {
        super.onRestoreInstanceState(load);
        String msg = "apiplayground loaded is Bundle save state";
        Log.i(appLogTag, msg);
        toastStatus(msg);
    }

    @Override
    protected void onSaveInstanceState(Bundle save) {
        super.onSaveInstanceState(save);
        String msg = "apiplayground state saved";
        Log.i(appLogTag, msg);
        toastStatus(msg);
    }

    public void toastStatus(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

}
