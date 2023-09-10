/* 'i' in Log.i sends an INFO log message to devices logger, viewable by logcat.
*  besides 'i', there are: Log.d (for debug), Log.e (error), Log.v (verbose), Log.w (warning),
* and Log.wtf() in extreme cases.
*
* function definition for common usage:
* Log.i(String customAppString, String messageToLog);
*
* Also, you can use println(int priority, String tag, String message) from normal java cmd line to produce logs.
*/

package com.crazyj36.logusage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;

public class MainActivity extends Activity {

    // A constant reference name to be used to find this apps log in your devices logcat messages.
    // search for this text when reading logs.
    private static final String appLogTag = "LOGUSAGETAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	   	setContentView(R.layout.activity_main);

    }

    // when using xml onClick must use view parameter to allow xml to reach java.
    public void logMessage(View view) {
        // puts message to devices' logger using an app tag and message.
        Log.i(appLogTag, "Log button clicked");
    }

}
