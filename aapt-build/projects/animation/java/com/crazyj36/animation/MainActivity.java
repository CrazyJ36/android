package com.crazyj36.animation;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.view.View;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.ImageView;
//import android.animation.ObjectAnimator;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	   	setContentView(R.layout.activity_main);

        //final Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation);
        //final ImageView imageView = findViewById(R.id.imageView);

        //imageView.startAnimation(animation);

        /*// On android sdk >= 11
        ObjectAnimator animation = ObjectAnimator.ofFloat(imageView, "translationX", 100f);
        animation.setDuration(1000);
        animation.start();*/

    }
}

