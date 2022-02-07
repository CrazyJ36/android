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

public class GameView extends View implements OnTouchListener {
    Context appContext;
    int x;
    int y;
    int score = 0;
    Handler handler = new Handler(Looper.getMainLooper());
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
        Button buttonLeft = ((Activity)appContext).findViewById(R.id.buttonLeft);
        Button buttonRight = ((Activity)appContext).findViewById(R.id.buttonRight);
        Button buttonUp = ((Activity)appContext).findViewById(R.id.buttonUp);
        Button buttonDown = ((Activity)appContext).findViewById(R.id.buttonDown);
        buttonLeft.setOnTouchListener(this);
        buttonRight.setOnTouchListener(this);
        buttonUp.setOnTouchListener(this);
        buttonDown.setOnTouchListener(this);
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
    }
        // FindViewById while this class is added to main layout and loaded by MainActivity.
        // Requires matching up this classes' context with an entire appContext.


/*        buttonLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent buttonLeftEvent) {
		        Runnable onLeftPressed = new Runnable() {
		            @Override
		            public void run() {
		                x = x - 5;
				        canvas.drawCircle(x, y, 24, paint);
                        handler.postDelayed(this, 5);
						if (buttonLeftEvent.getAction() == MotionEvent.ACTION_UP) {
                            handler.removeCallbacks(this);
                        }
		            }
		        };
                if (buttonLeftEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.postDelayed(onLeftPressed, 5);
                }
                return false;
            }
       });
        buttonRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent buttonRightEvent) {
		        Runnable onRightPressed = new Runnable() {
		            @Override
		            public void run() {
		                x = x + 5;
				        canvas.drawCircle(x, y, 24, paint);
                        handler.postDelayed(this, 5);
						if (buttonRightEvent.getAction() == MotionEvent.ACTION_UP) {
                            handler.removeCallbacks(this);
                        }
		            }
		        };
                if (buttonRightEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.postDelayed(onRightPressed, 5);
                }
                return false;
            }
       });
        buttonUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent buttonUpEvent) {
		        Runnable onUpPressed = new Runnable() {
		            @Override
		            public void run() {
		                y = y - 5;
				        canvas.drawCircle(x, y, 24, paint);
                        handler.postDelayed(this, 5);
						if (buttonUpEvent.getAction() == MotionEvent.ACTION_UP) {
                            handler.removeCallbacks(this);
                        }
		            }
		        };
                if (buttonUpEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.postDelayed(onUpPressed, 5);
                }
                return false;
            }
       });
        buttonDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent buttonDownEvent) {
		        Runnable onDownPressed = new Runnable() {
		            @Override
		            public void run() {
		                y = y + 5;
				        canvas.drawCircle(x, y, 24, paint);
                        handler.postDelayed(this, 5);
						if (buttonDownEvent.getAction() == MotionEvent.ACTION_UP) {
                            handler.removeCallbacks(this);
                        }
		            }
		        };
                if (buttonDownEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.postDelayed(onDownPressed, 5);
                }
                return false;
            }
       });*/

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (view.getId()) {
            case R.id.buttonLeft:
	            Runnable onLeftPressed = new Runnable() {
		            @Override
		            public void run() {
		                x = x - 5;
                        handler.postDelayed(this, 5);
						if (event.getAction() == MotionEvent.ACTION_UP) {
                            handler.removeCallbacks(this);
                        }
		            }
			    };
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.postDelayed(onLeftPressed, 5);
                }
				break;
            case R.id.buttonRight:
		        Runnable onRightPressed = new Runnable() {
		            @Override
		            public void run() {
		                x = x + 5;
                        handler.postDelayed(this, 5);
						if (event.getAction() == MotionEvent.ACTION_UP) {
                            handler.removeCallbacks(this);
                        }
		            }
			    };
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.postDelayed(onRightPressed, 5);
                }
                break;
            case R.id.buttonUp:
		        Runnable onUpPressed = new Runnable() {
		            @Override
		            public void run() {
		                y = y - 5;
                        handler.postDelayed(this, 5);
						if (event.getAction() == MotionEvent.ACTION_UP) {
                            handler.removeCallbacks(this);
                        }
		            }
			    };
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.postDelayed(onUpPressed, 5);
                }
                break;
            case R.id.buttonDown:
		        Runnable onDownPressed = new Runnable() {
		            @Override
		            public void run() {
		                y = y + 5;
                        handler.postDelayed(this, 5);
						if (event.getAction() == MotionEvent.ACTION_UP) {
                            handler.removeCallbacks(this);
                        }
		            }
			    };
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.postDelayed(onDownPressed, 5);
                }
                break;
        }
        return false;
    }

}

