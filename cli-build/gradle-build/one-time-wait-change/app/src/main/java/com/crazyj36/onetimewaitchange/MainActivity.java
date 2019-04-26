package com.crazyj36.onetimewaitchange;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.os.Handler;
import java.lang.Runnable;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   	    setContentView(R.layout.activity_main);
		
        final TextView textView = findViewById(R.id.textView);
		textView.setText("Wait Three Seconds");
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				textView.setText("Some New Text"); // Will happen after 3 seconds.
			}
		}, 3000);
    }
}
