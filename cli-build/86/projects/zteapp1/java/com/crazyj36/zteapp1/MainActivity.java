package com.crazyj36.zteapp1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import java.util.Date;
import android.view.View;
import android.content.Intent;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   	setContentView(R.layout.activity_main);

        // Using A random java package(java.util.Date), supposedly any normal java api can be used in android.
        TextView tv = findViewById(R.id.tv);
	String dateStr = new Date().toString();
        tv.setText(dateStr);

	Button btn = findViewById(R.id.btn);
	btn.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View view) {
		Intent activity2Intent = new Intent(MainActivity.this, Main2Activity.class);
		startActivity(activity2Intent);
            }
        });

        // Show all code has executed successfully
        Toast.makeText(getApplicationContext(), "test text", Toast.LENGTH_SHORT).show();
    }
}
