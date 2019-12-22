package com.crazyj36.logtoadb;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button logBtn = findViewById(R.id.logBtn);
        logBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String logChannel = "APIPLAYGROUNDLOG";
                Log.i(logChannel, "Log Button Clicked");
            }
        });
    }
}