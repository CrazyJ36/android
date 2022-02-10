package com.crazyj36.avoidmeteors;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Color;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import java.lang.Thread;
import com.crazyj36.avoidmeteors.MainActivity;

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
    Thread leftThread;
    Thread rightThread;
    Thread upThread;
    Thread downThread;
    int frames = 999999; // max nanoseconds.
    Paint paint = new Paint();
    AlertDialog.Builder dialogBuilder;
    MainActivity mainActivity = new MainActivity();

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        appContext = context;
        displayMetrics = appContext.getApplicationContext().getResources().getDisplayMetrics();
	    x = displayMetrics.widthPixels / 2;
	    y = displayMetrics.heightPixels / 2;
		dialogBuilder = new AlertDialog.Builder(appContext);
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
                frames = 999999;
			    x = displayMetrics.widthPixels / 2;
			    y = displayMetrics.heightPixels / 2;
	            enemyX = 10;
	            enemyY = 10;
	            GameView.this.setWillNotDraw(false);
	       }
	    });
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
        if (frames > 0) frames = frames = frames - 50; // lower the sleep nanoseconds.
        TextView scoreView = ((Activity)appContext).findViewById(R.id.scoreView);
        score = score + 1;
        scoreView.setText(String.valueOf(score) + " " + String.valueOf(frames));
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
        canvas.drawCircle(x, y - 28, 8, paint); // head
        // enemy
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        random++;
        enemyY = enemyY + 10;
        if (random % 5 == 0) enemyX = enemyX + 20;
        else enemyX = enemyX + 10;
        if (enemyX >= (97 * displayMetrics.widthPixels / 100)) enemyX = 2 * displayMetrics.widthPixels / 100;
        if (enemyY >= displayMetrics.heightPixels * 0.7) enemyY = 1 * displayMetrics.heightPixels / 100;
        canvas.drawCircle(enemyX, enemyY, 4, paint);
        // get hit
        if (( (enemyX >= x - 16 && enemyX <= x + 16) && (enemyY >= y - 32 && enemyY <= y + 16) ) ||
            ( (enemyX + 1 >= x - 16 && enemyX + 1 <= x + 16) && (enemyY + 1 >= y - 32 && enemyY + 1 <= y + 16) ) ||
            ( (enemyX + 2 >= x - 16 && enemyX + 2 <= x + 16) && (enemyY + 2 >= y - 32 && enemyY + 2 <= y + 16) ) ||
            ( (enemyX + 3 >= x - 16 && enemyX + 3 <= x + 16) && (enemyY + 3 >= y - 32 && enemyY + 3 <= y + 16) ) ||
            ( (enemyX + 4 >= x - 16 && enemyX + 4 <= x + 16) && (enemyY + 4 >= y - 32 && enemyY + 4 <= y + 16) ) ||
            ( (enemyX + 5 >= x - 16 && enemyX + 5 <= x + 16) && (enemyY + 5 >= y - 32 && enemyY + 5 <= y + 16) ) ||
            ( (enemyX + 6 >= x - 16 && enemyX + 6 <= x + 16) && (enemyY + 6 >= y - 32 && enemyY + 6 <= y + 16) ) ||
            ( (enemyX + 7 >= x - 16 && enemyX + 7 <= x + 16) && (enemyY + 7 >= y - 32 && enemyY + 7 <= y + 16) ) ||
            ( (enemyX + 8 >= x - 16 && enemyX + 8 <= x + 16) && (enemyY + 8 >= y - 32 && enemyY + 8 <= y + 16) ) ) {
            GameView.this.setWillNotDraw(true);
		    dialogBuilder.setTitle("You're Hit!").setMessage("Score: " + String.valueOf(score)).setCancelable(false);
            dialogBuilder.create().show();
        }
        // refresh screen for new frames
        invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        leftThread = new Thread() {
            public void run() {
                while (!leftThread.isInterrupted()) {
			        if ((view.getId() == R.id.buttonLeft) && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
			            // an "up" toast equires Looper.prepare() to be called before this thread.
			            leftThread.interrupt();
			            return;
			        }
                    try {
                        if (!leftThread.isInterrupted()) leftThread.sleep(1, frames); // stackoverflow if not running.
                    } catch (InterruptedException interruptedException) {
                        mainActivity.runOnUiThread(new Runnable() {
                            public void run() { Toast.makeText(appContext, "buttonLeft: Interrupted during thread sleep", Toast.LENGTH_LONG).show(); };
                        });
						return;
                    }
                    if (!leftThread.isAlive()) {
	    				x--;
                        leftThread.run();
                    }
                }
            }
        };
        rightThread = new Thread() {
            public void run() {
                while (!rightThread.isInterrupted()) {
			        if ((view.getId() == R.id.buttonRight) && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
			            rightThread.interrupt();
			            return;
			        }
                    try {
                        if (!rightThread.isInterrupted()) rightThread.sleep(1, frames);
                    } catch (InterruptedException interruptedException) {
                        mainActivity.runOnUiThread(new Runnable() {
                            public void run() { Toast.makeText(appContext, "buttonRight: interrupted during thread sleep", Toast.LENGTH_LONG).show(); };
                        });
						return;
                    }
                    if (!rightThread.isAlive()) {
                        x++;
                        rightThread.run();
                    }
                }
            }
        };
        upThread = new Thread() {
            public void run() {
                while (!upThread.isInterrupted()) {
			        if ((view.getId() == R.id.buttonUp) && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
			            upThread.interrupt();
			            return;
			        }
                    try {
                        if (!rightThread.isInterrupted()) upThread.sleep(1, frames);
                    } catch (InterruptedException interruptedException) {
                        mainActivity.runOnUiThread(new Runnable() {
                            public void run() { Toast.makeText(appContext, "buttonUp: Interrupted during thread sleep", Toast.LENGTH_LONG).show(); };
                        });
						return;
                    }
                    if (!upThread.isAlive()) {
                        y--;
                        upThread.run();
                    }
                }
            }
        };
        downThread = new Thread() {
            public void run() {
                while (!downThread.isInterrupted()) {
			        if ((view.getId() == R.id.buttonDown) && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
			            downThread.interrupt();
			            return;
			        }
                    try {
                        if (!downThread.isInterrupted()) downThread.sleep(1, frames);
                    } catch (InterruptedException interruptedException) {
                        mainActivity.runOnUiThread(new Runnable() {
                            public void run() { Toast.makeText(appContext, "buttonDown: Interrupted during thread sleep", Toast.LENGTH_LONG).show(); };
                        });
						return;
                    }
                    if (!downThread.isAlive()) {
                        y++;
                        downThread.run();
                    }
                }
            }
        };
        // Double check that nothing is running at once.
        /*if ((view.getId() == R.id.buttonLeft || view.getId() == R.id.buttonRight || view.getId() == R.id.buttonUp || view.getId() == R.id.buttonDown ) &&
             (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
               leftThread.interrupt();
               rightThread.interrupt();
               upThread.interrupt();
               downThread.interrupt();
        }*/
        switch(view.getId()) {
            case R.id.buttonLeft:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x--;
	                leftThread.start();
                }
                break;
            case R.id.buttonRight:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x++;
                    rightThread.start();
                }
                break;
            case R.id.buttonUp:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    y--;
                    upThread.start();
                }
                break;
            case R.id.buttonDown:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    y++;
                    downThread.start();
                }
                break;
        }
        return false;
    }
}

