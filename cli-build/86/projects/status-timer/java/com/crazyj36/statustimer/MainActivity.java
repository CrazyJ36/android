package com.crazyj36.statustimer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);
        textView.setText("Space");

        Button button = findViewById(R.id.button);
        button.setText("Start");

    }
}
