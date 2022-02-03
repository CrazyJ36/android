package com.crazyj36.animation;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.Button;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	   	setContentView(R.layout.activity_main);

  	}

    public void move(View view) {
    	ImageView imageView = findViewById(R.id.imageView);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.move);
        imageView.startAnimation(animation);

        Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
    }
}

