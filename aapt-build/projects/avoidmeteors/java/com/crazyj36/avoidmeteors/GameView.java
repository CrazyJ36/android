package com.crazyj36.avoidmeteors;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.view.MotionEvent;
import android.view.LayoutInflater;
import android.widget.Toast;
import android.widget.TextView;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import com.crazyj36.avoidmeteors.MainActivity;

public class GameView extends View {
    int currentX = 480;
    int currentY = 480;
    int newX = 0;
    int newY = 0;


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
        paint.setColor(Color.GREEN);
        canvas.drawCircle(50, 50, 24, paint);

        // FindViewById while this class is added to main layout and loaded by MainActivity.
        // Requires matching up this classes' context with an entire appContext.
        TextView textView = (TextView) ((Activity)appContext).findViewById(R.id.textView);
        textView.setText(String.valueOf(currentX));
        Button buttonLeft = (Button) ((Activity)appContext).findViewById(R.id.buttonLeft);

        buttonLeft.setOnTouchListener(new View.OnTouchListener() {
            Handler handler;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (handler != null) return true;
                        handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(onLeftPressed, 100);
                        break;
                    case MotionEvent.ACTION_UP:
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
		            currentX = currentX - 1;
	                newX = currentX;
	                textView.setText(String.valueOf(newX));
	                handler.postDelayed(this, 100);
	            }
	        };
        });
    }
}

