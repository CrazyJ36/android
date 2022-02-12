package com.crazyj36.avoidmeteors;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.AttributeSet;
import java.lang.Thread;

public class GameView extends View {
    static DisplayMetrics displayMetrics;
    static float x; // static so it can be changed by MainActivity onTouch().
    static float y;
    static float enemyX = 0;
    static float enemyY = 10;
    static int random = 0;
    static int score = 0;
    static int characterSpeed = 999999;
    long milliseconds = 50;
    int nanoseconds = 999999;
    boolean resetNanoseconds = false;
    boolean isMiddleSpeed = false;
    Paint paint = new Paint();
    AlertDialog.Builder dialogBuilder;
    Thread thread;

    public float dp(float desired) {
        float scale = GameView.this.getResources().getDisplayMetrics().density;
        return desired * scale + 0.5f;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // loop sleep
        thread = new Thread() {
            public void run() {
                try {
                    if (milliseconds > 0 && nanoseconds > 0) {
                        Thread.sleep(milliseconds, nanoseconds);
                        milliseconds--;
                        nanoseconds = nanoseconds - 10000;
                    }

                    else if (milliseconds <= 0 && nanoseconds > 0 && resetNanoseconds) {
                    	Thread.sleep(0, nanoseconds);
                    	nanoseconds = nanoseconds - 10000;
                    }
                    if (milliseconds <= 0 && nanoseconds <= 0) {
                        resetNanoseconds = true;
                        nanoseconds = 999999;
                        MainActivity.toaster("Watch out!");
                    }
                    if (milliseconds == 0 && nanoseconds == 0) MainActivity.toaster("You've Won!");
                } catch (InterruptedException interruptedException) {
                    MainActivity.toaster(interruptedException.getMessage());
                }
                thread.interrupt();
                return;
            }
        };
        // character start.
        displayMetrics = context.getResources().getDisplayMetrics();
        x = displayMetrics.widthPixels / 2;
        y = displayMetrics.heightPixels / 2 - dp(128);
        // game dialog and reset logic.
		dialogBuilder = new AlertDialog.Builder(context);
	    dialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            dialog.cancel();
	        }
	    });
	    dialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
	        @Override
	        public void onCancel(DialogInterface onCancelDialog) {
                score = 0;
                characterSpeed = 999999;
                milliseconds = 50;
                nanoseconds = 999999;
                //isMaxSpeed = false;
                //isMaxSpeedSaid = false;
			    x = displayMetrics.widthPixels / 2;
			    y = displayMetrics.heightPixels / 2 - dp(128);
	            enemyX = 0;
	            enemyY = 10;
	            GameView.this.setWillNotDraw(false);
	       }
	    });
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        score = score + 1;
        if (characterSpeed > 0) characterSpeed = characterSpeed - 10; // lower the characterSpeed sleep nanoseconds.
        MainActivity.scoreView.setText(GameView.this.getResources().getString(R.string.scoreText) + String.valueOf(score) + "\n" + "Speed: " + String.valueOf(milliseconds) + " " + String.valueOf(nanoseconds));
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(dp(4)); // convert px to dp for actual drawing.
        // outline
        canvas.drawRect( (float)2 * displayMetrics.widthPixels / 100, // left screen percentage
                         (float)1 * displayMetrics.heightPixels / 100, // top screen percentage
                         (float)98 * displayMetrics.widthPixels / 100, // right screen percentage
                         (displayMetrics.heightPixels * 0.7f) - dp(86), // bottom screen percentage
                         paint);
        // character
        float pts[] = {x, y, x, y - dp(8), // body
                       x, y, x + dp(6), y + dp(8), // right leg
                       x, y, x - dp(6), y + dp(8), // left leg
                       x, y - dp(8), x + dp(8), y - dp(10),  // right arm
                       x, y - dp(8), x - dp(8), y - dp(10)}; // left arm
        canvas.drawCircle(x, y - dp(15), dp(3), paint); // head
        canvas.drawLines(pts, paint);
        // enemy
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        random++;
        if (random % 5 == 0) {
            enemyX = enemyX + dp(10);
        } else {
            enemyX = enemyX + dp(4);
        }
        enemyY = enemyY + dp(8);
        /*If ( gameSpeedInc % 50 == 0) {
           if (gameSpeed < dp(30)) {
               gameSpeed++;
               enemyY = enemyY + (gameSpeed * 0.5f);
           } else isMaxSpeed = true;
           if (isMaxSpeed) {
               if (!isMaxSpeedSaid) {
                   MainActivity.toaster("Watch out!");
                   isMaxSpeedSaid = true;
               }
           }
        }*/
        //gameSpeedInc++;
        if (enemyX >= (97 * displayMetrics.widthPixels / 100)) enemyX = 2 * displayMetrics.widthPixels / 100;
        if (enemyY >= displayMetrics.heightPixels * 0.7f - dp(86)) enemyY = 1 * displayMetrics.heightPixels / 100;
        canvas.drawCircle(enemyX, enemyY, dp(3), paint);
        // get hit
        if (( (enemyX >= x - dp(8) && enemyX <= x + dp(8)) && (enemyY >= y - dp(8) && enemyY <= y + dp(8)) ) ||
            ( (enemyX + dp(1) >= x - dp(8) && enemyX + dp(1) <= x + dp(8)) && (enemyY + dp(1) >= y - dp(15) && enemyY + dp(1) <= y + dp(16)) ) ||
            ( (enemyX + dp(2) >= x - dp(8) && enemyX + dp(2) <= x + dp(8)) && (enemyY + dp(2) >= y - dp(15) && enemyY + dp(2) <= y + dp(16)) ) ||
            ( (enemyX + dp(3) >= x - dp(8) && enemyX + dp(3) <= x + dp(8)) && (enemyY + dp(3) >= y - dp(15) && enemyY + dp(3) <= y + dp(16)) ) ) {
            GameView.this.setWillNotDraw(true);
            MainActivity.scoreView.setText("");
		    dialogBuilder.setTitle("You're Hit!").setMessage("Score: " + String.valueOf(score)).setCancelable(false);
            dialogBuilder.create().show();
        }
        // out of bounds
        if ( (x + dp(8)) >= (98 * displayMetrics.widthPixels / 100) || // right bounds
             (x - dp(6)) <= (2 * displayMetrics.widthPixels / 100) || // left
             (y + dp(8)) >= (displayMetrics.heightPixels * 0.7f - dp(86)) || // bottom bounds
             (y - dp(15)) <= (1 * displayMetrics.heightPixels / 100) ) {  // top
            GameView.this.setWillNotDraw(true);
            MainActivity.scoreView.setText("");
            dialogBuilder.setTitle("Out of Bounds!").setMessage("Try again.").setCancelable(false);
            dialogBuilder.create().show();
        }
        // invalidate current painting for new frames.
        thread.run();
        invalidate();
    }

}

