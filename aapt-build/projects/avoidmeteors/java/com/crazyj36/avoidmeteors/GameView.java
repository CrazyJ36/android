package com.crazyj36.avoidmeteors;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.View;
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

public class GameView extends View {
    Context appContext;
    int x = 480;
    int y = 480;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.appContext = context;
        // Build A new layout programatically from MainActivity by extending A view such as RelativeLayot in this class.
        /*buttonLeft = new Button(context);
        buttonLeft.setGravity(50);
        buttonLeft.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        buttonLeft.setText("L");
	    textView = new TextView(context);
        addView(buttonLeft);*/
	}

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // FindViewById while this class is added to main layout and loaded by MainActivity.
        // Requires matching up this classes' context with an entire appContext.
        TextView textView = ((Activity)appContext).findViewById(R.id.textView);
        Button buttonLeft = ((Activity)appContext).findViewById(R.id.buttonLeft);
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        Handler handler = new Handler(Looper.getMainLooper());
        canvas.drawCircle(x, y, 24, paint);
	    textView.setText(String.valueOf(x));

        buttonLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
		        Runnable onLeftPressed = new Runnable() {
		            @Override
		            public void run() {
                        invalidate();
		                x = x - 1;
				        canvas.drawCircle(x, y, 24, paint);
					    textView.setText(String.valueOf(x));
                        handler.postDelayed(this, 5);
						if (event.getAction() == MotionEvent.ACTION_UP) {
                            Toast.makeText(appContext, "Up", Toast.LENGTH_SHORT).show();
                            handler.removeCallbacks(this);
                        }
		            }
		        };
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(appContext, "Down", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(onLeftPressed, 5);
                }
                //handler.removeCallbacks(onLeftPressed);
                //handler.postDelayed(onLeftPressed, 5);
                return false;
            }
       });
    }
}

