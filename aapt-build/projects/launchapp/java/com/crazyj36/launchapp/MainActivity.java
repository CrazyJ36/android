package com.crazyj36.launchapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;
import android.content.ActivityNotFoundException;
import java.lang.NullPointerException;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	   	setContentView(R.layout.activity_main);

        findViewById(R.id.btnLaunch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Easiest, probably best way to launch A package.
            // First check that the desired package exists.
                try {
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.settings");
                    // Could also check the launchIntent != null.
                    startActivity(launchIntent);

                    // In english: catch Exception or Exception as variable
                } catch (ActivityNotFoundException | NullPointerException mException) {
                    Toast.makeText(MainActivity.this, "com.android.settings app not found:\n" + mException.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
