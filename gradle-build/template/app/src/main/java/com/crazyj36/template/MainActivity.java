package com.crazyj36.template;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   	setContentView(R.layout.activity_main);

        TextView text = (TextView) findViewById(R.id.textView);
        text.setText("Template Gradle CLI Built App");
        
    }
}
