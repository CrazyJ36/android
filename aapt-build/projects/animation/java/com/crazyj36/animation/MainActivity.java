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

        ImageView imageView = findViewById(R.id.imageView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
                imageView.startAnimation(animation);

                /*
                // On android sdk >= 11
                ObjectAnimator animation = ObjectAnimator.ofFloat(imageView, "translationX", 100f);
                animation.setDuration(1000);
                animation.start();
                */
            }
        });

    }
}

