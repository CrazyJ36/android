package com.crazyj36.animation;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
//import android.animation.ObjectAnimator;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	   	setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        ImageView imageView = findViewById(R.id.imageView);
        // Load all animations and start them programatically below.
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.startAnimation(animation1);
                /* On android sdk >= 11
                ObjectAnimator animation = ObjectAnimator.ofFloat(imageView, "translationX", 100f);
                animation.setDuration(1000);
                animation.start();
                */
            }
        });
        // When onClick animation is done, load another res/anim file.
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation1) {
            }
            @Override
            public void onAnimationEnd(Animation animation1) {
                imageView.startAnimation(animation2);
            }
            @Override
            public void onAnimationRepeat(Animation animation1) {
            }
        });
    }

}

