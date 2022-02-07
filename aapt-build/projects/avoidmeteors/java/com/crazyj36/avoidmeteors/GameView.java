package com.crazyj36.avoidmeteors;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnTouchListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.widget.Button;
import android.view.MotionEvent;
import android.widget.Toast;
import android.widget.TextView;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

public class GameView extends View {
    Context appContext;
    int x;
    int y;
    int score = 0;
    Button buttonLeft;
    Button buttonRight;
    Button buttonUp;
    Button buttonDown;
    Handler handler = new Handler(Looper.getMainLooper());
    Runnable onLeftPressed;
    Runnable onRightPressed;

    Paint paint = new Paint();

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        appContext = context;
	    DisplayMetrics displayMetrics = context.getApplicationContext().getResources().getDisplayMetrics();
	    x = displayMetrics.widthPixels / 2;
	    y = displayMetrics.heightPixels / 2;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // FindViewById while this class is added to main layout and loaded by MainActivity.
        // Requires matching up this classes' context with an entire appContext.
        buttonLeft = ((Activity)appContext).findViewById(R.id.buttonLeft);
        buttonRight = ((Activity)appContext).findViewById(R.id.buttonRight);
        buttonUp = ((Activity)appContext).findViewById(R.id.buttonUp);
        buttonDown = ((Activity)appContext).findViewById(R.id.buttonDown);

        TextView scoreView = ((Activity)appContext).findViewById(R.id.scoreView);

        // Build A new layout programatically from MainActivity by extending A view such as RelativeLayot in this class.
        /*buttonLeft = new Button(context);
        buttonLeft.setGravity(50);
        buttonLeft.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        buttonLeft.setText("L");
	    textView = new TextView(context);
        addView(buttonLeft);*/
        score = score + 1;
        scoreView.setText(String.valueOf(score));
        paint.setColor(Color.GRAY);
        canvas.drawCircle(x, y, 24, paint);
        invalidate();

        buttonLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent buttonLeftEvent) {
		        Runnable onLeftPressed = new Runnable() {
		            @Override
		            public void run() {
						if (buttonLeftEvent.getAction() == MotionEvent.ACTION_UP) {
                            handler.removeCallbacks(this);
                            return;
                        }
                        x = x - 5;
                        handler.postDelayed(this, 10);
		            }
		        };
                handler.postDelayed(onLeftPressed, 10);
                return false;
            }
        });
        buttonRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent buttonRightEvent) {
		        Runnable onRightPressed = new Runnable() {
		            @Override
		            public void run() {
						if (buttonRightEvent.getAction() == MotionEvent.ACTION_UP) {
                            handler.removeCallbacks(this);
                            return;
                        }
                        x = x + 5;
                        handler.postDelayed(this, 10);
		            }
		        };
                handler.postDelayed(onRightPressed, 10);
                return false;
            }
        });
        buttonUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent buttonUpEvent) {
		        Runnable onUpPressed = new Runnable() {
		            @Override
		            public void run() {
						if (buttonUpEvent.getAction() == MotionEvent.ACTION_UP) {
                            handler.removeCallbacks(this);
                            return;
                        }
		                y = y - 5;
                        handler.postDelayed(this, 10);
		            }
		        };
                handler.postDelayed(onUpPressed, 10);
                return false;
            }
        });
        buttonDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent buttonDownEvent) {
		        Runnable onDownPressed = new Runnable() {
		            @Override
		            public void run() {
						if (buttonDownEvent.getAction() == MotionEvent.ACTION_UP) {
                            handler.removeCallbacks(this);
                            return;
                        }
		                y = y + 5;
                        handler.postDelayed(this, 10);
		            }
		        };
                handler.postDelayed(onDownPressed, 10);
                return false;
            }
        });

    }
}

