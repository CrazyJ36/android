package com.crazyj36.wearnews;

import static com.crazyj36.wearnews.UpdateService.timer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainPhoneActivity extends Activity {
    static TextView info;
    static Context context;
    Intent intent;
    static boolean restart = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
        info = findViewById(R.id.info);
        intent = new Intent(this, UpdateService.class);
        findViewById(R.id.startServiceButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restart = true;
                startForegroundService(intent);
            }
        });
        findViewById(R.id.stopServiceBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restart = false;
                stopService(intent);
                timer.cancel();
                info.setText("");
                finishAffinity();
            }
        });
    }
    public static void setInfoText(String text) {
        context.getMainExecutor().execute(new Runnable() {
            @Override
            public void run() {info.setText(text);}
        });
    }
    @Override
    public void onResume () {super.onResume();}
    @Override
    public void onPause () {super.onPause();}
    @Override
    protected void onDestroy() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        if (MainPhoneActivity.restart) {
            this.sendBroadcast(broadcastIntent);
        } else {
            info.setText("");
            stopService(broadcastIntent);
            Log.d("WEARNEWS", "service destroyed");
        }
        super.onDestroy();
    }
}


