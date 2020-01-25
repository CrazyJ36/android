package com.crazyj36.startspecificactivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.content.ComponentName;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	   	setContentView(R.layout.activity_main);
    }

    public void btnLaunchClick(View v) {
        Intent launchIntent = new Intent();
        ComponentName componentName = new ComponentName(
            "com.android.settings",
            "com.android.settings.Settings$DeviceInfoSettingsActivity"
        );
        launchIntent.setComponent(componentName);
        startActivity(launchIntent);
    }

}
