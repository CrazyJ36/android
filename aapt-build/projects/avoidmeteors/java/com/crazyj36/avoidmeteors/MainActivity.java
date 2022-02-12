package com.crazyj36.avoidmeteors;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.content.Context;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import java.lang.Thread;

public class MainActivity extends Activity implements OnTouchListener {
    Button buttonLeft;
    Button buttonRight;
    Button buttonUp;
    Button buttonDown;
    static TextView scoreView; // static enables variables to be accessed by other classes such as GameView
    Thread threadLeft;
    Thread threadRight;
    Thread threadUp;
    Thread threadDown;
    private static Context context;
    // GameView gameView; also findViewById(R.id.gameView), then gameView.method() besides sharing static variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
	    setContentView(R.layout.activity_main);
        scoreView = findViewById(R.id.scoreView);
        buttonLeft = findViewById(R.id.buttonLeft);
        buttonRight = findViewById(R.id.buttonRight);
        buttonUp = findViewById(R.id.buttonUp);
        buttonDown = findViewById(R.id.buttonDown);
        buttonLeft.setOnTouchListener(this);
        buttonRight.setOnTouchListener(this);
        buttonUp.setOnTouchListener(this);
        buttonDown.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        threadLeft = new Thread() {
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
	    		    GameView.x--;
			        if ((view.getId() == R.id.buttonLeft) && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
			            Thread.currentThread().interrupt();
			            return;
			        }
                    try {
                        Thread.sleep(1, GameView.characterSpeed);
                    } catch (InterruptedException interruptedException) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, "buttonLeft: Attempted to sleep an interrupted thread, error.", Toast.LENGTH_LONG).show();
                            }
                        });
						return;
                    }
                    Thread.currentThread().run();
                }
            }
        };
        threadRight = new Thread() {
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    GameView.x++;
			        if ((view.getId() == R.id.buttonRight) && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
			            Thread.currentThread().interrupt();
			            return;
			        }
                    try {
                        Thread.sleep(1, GameView.characterSpeed);
                    } catch (InterruptedException interruptedException) {
                        runOnUiThread(new Runnable() {
                            public void run() { Toast.makeText(MainActivity.this, "buttonRight: Attempted to sleep an interrupted thread, error", Toast.LENGTH_LONG).show(); };
                        });
						return;
                    }
                    Thread.currentThread().run();
                }
            }
        };
        threadUp = new Thread() {
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    GameView.y--;
			        if ((view.getId() == R.id.buttonUp) && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
			            Thread.currentThread().interrupt();
			            return;
			        }
                    try {
                        Thread.sleep(1, GameView.characterSpeed);
                    } catch (InterruptedException interruptedException) {
                        runOnUiThread(new Runnable() {
                            public void run() { Toast.makeText(MainActivity.this, "buttonUp: Attempted to sleep an interrupted thread, error.", Toast.LENGTH_LONG).show(); };
                        });
						return;
                    }
                    Thread.currentThread().run();
                }
            }
        };
        threadDown = new Thread() {
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    GameView.y++;
			        if ((view.getId() == R.id.buttonDown) && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
			            Thread.currentThread().interrupt();
			            return;
			        }
                    try {
                        Thread.sleep(1, GameView.characterSpeed);
                    } catch (InterruptedException interruptedException) {
                        runOnUiThread(new Runnable() {
                            public void run() { Toast.makeText(MainActivity.this, "buttonDown: Attempted to sleep an interrupted thread, error.", Toast.LENGTH_LONG).show(); };
                        });
						return;
                    }
                    Thread.currentThread().run();
                }
            }
        };
        switch(view.getId()) {
            case R.id.buttonLeft:
                if (view.getId() == R.id.buttonLeft && event.getAction() == MotionEvent.ACTION_DOWN) {
                    //GameView.x--;
                    threadLeft.start();
                }
                if (view.getId() == R.id.buttonLeft && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                    threadLeft.interrupt();
                }
                break;
            case R.id.buttonRight:
                if (view.getId() == R.id.buttonRight && event.getAction() == MotionEvent.ACTION_DOWN) {
                    GameView.x++;
                    threadRight.start();
                }
                if (view.getId() == R.id.buttonRight && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                    threadRight.interrupt();
                }
                break;
            case R.id.buttonUp:
                if (view.getId() == R.id.buttonUp && event.getAction() == MotionEvent.ACTION_DOWN) {
                    GameView.y--;
                    threadUp.start();
                }
                if (view.getId() == R.id.buttonUp && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                    threadUp.interrupt();
                }
                break;
            case R.id.buttonDown:
                if (view.getId() == R.id.buttonDown && event.getAction() == MotionEvent.ACTION_DOWN) {
                    GameView.y++;
                    threadDown.start();
                }
                if (view.getId() == R.id.buttonDown && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                    threadDown.interrupt();
                }
                break;
        }
        return false;
    }

    public static void toaster(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
