package com.crazyj36.wearnews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.Objects;

public class MainPhoneActivity extends Activity {
    Intent intent;
    public static ThreadLocal<TextView> info = new ThreadLocal<>();
    public static ThreadLocal<TextView> info2 = new ThreadLocal<>();
    public static ThreadLocal<TextView> info3 = new ThreadLocal<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info.set(findViewById(R.id.info));
        info2.set(findViewById(R.id.info2));
        info3.set(findViewById(R.id.info3));
        intent = new Intent(this, UpdateService.class);
        findViewById(R.id.startServiceButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startForegroundService(intent);
            }
        });
        findViewById(R.id.stopServiceBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(intent);
                finishAffinity();
            }
        });
    }
    public static void setInfoText(Context context, String text) {
        context.getMainExecutor().execute(new Runnable() {
             @Override
             public void run() {
                Objects.requireNonNull(info.get()).setText(text);
             }
        });
    }
    public static void setInfo2Text(Context context, String text) {
        context.getMainExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Objects.requireNonNull(info2.get()).setText(text);
            }
        });
    }
    public static void setInfo3Text(Context context, String text) {
        context.getMainExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Objects.requireNonNull(info3.get()).setText(text);
            }
        });
    }
    @Override
    public void onResume () {super.onResume();}
    @Override
    public void onPause () {super.onPause();}
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


