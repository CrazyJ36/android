package com.crazyj36.launchapp;

import android.app.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launch_calculator(View view) {
        Intent launchContacts = getPackageManager().getLaunchIntentForPackage("com.android.calculator");
        if (launchContacts != null) {
            startActivity(launchContacts);
        } else {
            try {
                launchContacts = getPackageManager().getLaunchIntentForPackage("com.google.android.calculator");
                startActivity(launchContacts);
            } catch (ActivityNotFoundException activityNotFound) {
                Toast.makeText(this, "Activity not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void launch_contacts(View view) {
        Intent launchContacts = getPackageManager().getLaunchIntentForPackage("com.android.contacts");
        if (launchContacts != null) {
            startActivity(launchContacts);
        } else {
            try {
                launchContacts = getPackageManager().getLaunchIntentForPackage("com.google.android.contacts");
                startActivity(launchContacts);
            } catch (ActivityNotFoundException activityNotFound) {
                Toast.makeText(this, activityNotFound.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
         }

    }
}
