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
    DisplayMetrics displayMetrics;
    int x;
    int y;
    int enemyX = 10;
    int enemyY = 10;
    int random = 0;
    int score = 0;
    Button buttonLeft;
    Button buttonRight;
    Button buttonUp;
    Button buttonDown;
    Handler handler = new Handler(Looper.getMainLooper());
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
        paint.setStrokeWidth(8);
        // outline
        canvas.drawRect( (float)2 * displayMetrics.widthPixels / 100, // left screen percentage
                         (float)1 * displayMetrics.heightPixels / 100, // top screen percentage
                         (float)98 * displayMetrics.widthPixels / 100, // right screen percentage
                         (float)displayMetrics.heightPixels * 0.7f, // bottom screen percentage
                         paint);
        // character
        float pts[] = {x, y, x, y - 24, // body
                       x, y, x + 12, y + 16, // right leg
                       x, y, x - 12, y + 16, // left leg
                       x, y - 16, x + 18, y - 18,  // right arm
                       x, y - 16, x - 18, y - 18}; // left arm
        canvas.drawLines(pts, paint);
        canvas.drawCircle(x, y - 28, 4, paint); // head
        // enemy
        random++;
        enemyY = enemyY + 10;
        if (random % 5 == 0) enemyX = enemyX + 20;
        else enemyX = enemyX + 10;
        if (enemyX >= (98 * displayMetrics.widthPixels / 100)) enemyX = 6;
        if (enemyY >= displayMetrics.heightPixels * 0.7) enemyY = 6;
        canvas.drawCircle(enemyX, enemyY, 4, paint);
        // get hit
        if ((enemyX >= x - 16 && enemyX <= x) && (enemyY >= y - 18 && enemyY <= y + 16)) {
            GameView.this.setWillNotDraw(true);
        }
        // refresh
        invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch(view.getId()) {
            case R.id.buttonLeft:
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    move('x', '-', true);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                  move('x', '-', false);
                }
                break;
            case R.id.buttonRight:
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    move('x' , '+', true);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    move('x', '+', false);
                }
                break;
            case R.id.buttonUp:
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    move('y' , '-', true);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    move('y', '-', false);
                }
                break;
            case R.id.buttonDown:
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    move('y', '+', true);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    move('y', '+', false);
                }
                break;
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

