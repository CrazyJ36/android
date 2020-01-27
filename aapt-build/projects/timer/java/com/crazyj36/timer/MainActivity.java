package com.crazyj36.timer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	   	setContentView(R.layout.activity_main);

        final TextView tv = findViewById(R.id.tvCount);
        findViewById(R.id.btnStart).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                x = x + 1;
                                tv.setText(String.valueOf(x));
                                if (x == 10) {
                                    timer.cancel();
                                    tv.setText("Counted to 10");
                                    x = 0;
                                }
                            }
                        });
                    }
                }, 0, 1000);
            }
        });

    }
}
