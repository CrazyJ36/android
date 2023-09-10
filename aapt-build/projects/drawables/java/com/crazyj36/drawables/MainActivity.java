package com.crazyj36.drawables;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends Activity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	   	setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.character);
        imageView.setImageResource(R.drawable.character);
    }
}
