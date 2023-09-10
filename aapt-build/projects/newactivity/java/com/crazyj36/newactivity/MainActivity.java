package com.crazyj36.newactivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	   	setContentView(R.layout.activity_main);
    }

    public void newActivity(View v) {
        Intent nextActivity = new Intent(MainActivity.this, NextActivity.class);
        startActivity(nextActivity);
    }
}
