package com.example.hoverexample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnHoverListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import crazyj36.hoverproject.R;


public class MainActivity extends Activity {

	ImageView img;
    private Button button;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initSPen();
		
	}

	private void initSPen() {

        img = (ImageView) (findViewById(R.id.imageView1));
        final TextView tv = (TextView) (findViewById(R.id.textView1));
        img.setOnHoverListener(new OnHoverListener() {

            @Override
            public boolean onHover(View v, MotionEvent event) {
                Log.d("Hover", "On the image");
                if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                    tv.setText("Hovering, now the image is expaned");
                    img.animate().scaleX((float) (1.5)).scaleY((float) (1.5)).setDuration(100).withLayer();
                } else if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                    tv.setText("Not hovering over image");
                    img.animate().scaleX((float) (1)).scaleY((float) (1)).setDuration(100).withLayer();
                } else if (event.getButtonState() == MotionEvent.BUTTON_SECONDARY) {

                }
                return false;
            }
        });
        img.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("EVT", event.getActionIndex() + "");
                return false;
            }
        });
        button = (Button) (findViewById(R.id.btn1));
        button.setOnHoverListener(new OnHoverListener() {
            @Override
            public boolean onHover(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                    Intent showConfirm =
                            new Intent(MainActivity.this, confirm.class);
                    MainActivity.this.startActivity(showConfirm);
                } else if (motionEvent.getAction()== MotionEvent.ACTION_HOVER_EXIT) {
                    finish();
                }
                return false;
            }
        });
    }}