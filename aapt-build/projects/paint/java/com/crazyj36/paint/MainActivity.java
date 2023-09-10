/* example from: https://javatpoint.com/android-simple-graphics-example */

package com.crazyj36.paint;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MainActivity extends Activity {

    MyView myView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView to the below view containing the paint canvas code.
        myView = new MyView(this);
	   	setContentView(myView);
    }

	public class MyView extends View {
	    public MyView(Context context) {
	        super(context);
	    }
	    @Override
	    public void onDraw(Canvas canvas) {
	        super.onDraw(canvas);

	        Paint paint = new Paint();
	        paint.setColor(Color.GREEN);
	        canvas.drawCircle(50, 50, 24, paint);

	    }
	}

}

