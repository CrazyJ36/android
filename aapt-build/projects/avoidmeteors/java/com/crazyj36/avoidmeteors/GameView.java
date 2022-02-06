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
    int x = 480;
    int y = 480;

    Context appContext;
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
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        postInvalidate();
        canvas.drawCircle(x, y, 24, paint);

        // FindViewById while this class is added to main layout and loaded by MainActivity.
        // Requires matching up this classes' context with an entire appContext.
        TextView textView = ((Activity)appContext).findViewById(R.id.textView);
        textView.setText(String.valueOf(x));
        Button buttonLeft = ((Activity)appContext).findViewById(R.id.buttonLeft);

        buttonLeft.setOnTouchListener(new View.OnTouchListener() {
            private Handler handler;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (handler != null) return true;
                        handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(onLeftPressed, 50);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (handler == null) return true;
                        handler.removeCallbacks(onLeftPressed);
                        handler = null;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        if (handler == null) return true;
                        handler.removeCallbacks(onLeftPressed);
                        handler = null;
                        break;
                }
                return false;
	        }
            Runnable onLeftPressed = new Runnable() {
	            @Override
	            public void run() {
		            x = x - 1;
	                textView.setText(String.valueOf(x));
	                handler.postDelayed(this, 50);
	            }
	        };
        });
    }
}

