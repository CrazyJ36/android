package com.crazyj36.avoidmeteors;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnTouchListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Color;
import android.widget.Button;
import android.view.MotionEvent;
import android.widget.Toast;
import android.widget.TextView;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

public class GameView extends View implements OnTouchListener {
    Context appContext;
    int x;
    int y;
    int score = -1;
    Button buttonLeft;
    Button buttonRight;
    Button buttonUp;
    Button buttonDown;
    Handler handler = new Handler(Looper.getMainLooper());
	DisplayMetrics displayMetrics;
    Paint paint = new Paint();

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        appContext = context;
        displayMetrics = appContext.getApplicationContext().getResources().getDisplayMetrics();
	    x = displayMetrics.widthPixels / 2;
	    y = displayMetrics.heightPixels / 2;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        buttonLeft = ((Activity)appContext).findViewById(R.id.buttonLeft); // get entire appcontext
        buttonRight = ((Activity)appContext).findViewById(R.id.buttonRight);
        buttonUp = ((Activity)appContext).findViewById(R.id.buttonUp);
        buttonDown = ((Activity)appContext).findViewById(R.id.buttonDown);
        buttonLeft.setOnTouchListener(this);
        buttonRight.setOnTouchListener(this);
        buttonUp.setOnTouchListener(this);
        buttonDown.setOnTouchListener(this);
        TextView scoreView = ((Activity)appContext).findViewById(R.id.scoreView);
        score = score + 1;
        scoreView.setText(String.valueOf(score));
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        canvas.drawRect( (float)3 * displayMetrics.widthPixels / 100, // left
                         (float)1 * displayMetrics.heightPixels / 100, // top
                         (float)97 * displayMetrics.widthPixels / 100, // right
                         (float)displayMetrics.heightPixels * 0.7f, // bottom percent
                         paint);
        canvas.drawCircle(x, y, 24, paint);
        invalidate();
        // Build A new layout programatically from MainActivity by extending A view such as RelativeLayot in this class.
        /*buttonLeft = new Button(context);
        buttonLeft.setGravity(50);
        buttonLeft.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        buttonLeft.setText("L");
	    textView = new TextView(context);
        addView(buttonLeft);*/
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch(view.getId()) {
            case R.id.buttonLeft:
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    move('x', '-', true);
                    break;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                  move('x', '-', false);
                  break;
                } else break;
            case R.id.buttonRight:
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    move('x' , '+', true);
                    break;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    move('x', '+', false);
                    break;
                } else break;
            case R.id.buttonUp:
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    move('y' , '-', true);
                    break;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    move('y', '-', false);
                    break;
                } else break;
            case R.id.buttonDown:
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    move('y', '+', true);
                    break;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    move('y', '+', false);
                    break;
                } else break;
        }
        return false;
    }

    public void move(char xOrY, char posOrNeg, boolean stop) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (stop == true) {
                    handler.removeCallbacks(this);
                    if (handler.hasCallbacks(this)) Toast.makeText(appContext, "stray callbacks", Toast.LENGTH_SHORT).show();
                } else if (stop == false) {
		            if (xOrY == 'x' && posOrNeg == '+') x = x + 1;
		            else if (xOrY == 'x' && posOrNeg == '-') x = x - 1;
		            else if (xOrY == 'y' && posOrNeg == '-') y = y - 1;
		            else if (xOrY == 'y' && posOrNeg == '+') y = y + 1;
                    handler.postDelayed(this, 1);
                }
            }
        }, 1);
    }

}

