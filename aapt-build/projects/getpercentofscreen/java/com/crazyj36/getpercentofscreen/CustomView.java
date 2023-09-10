package com.crazyj36.getpercentofscreen;

import android.view.View;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.graphics.Paint;
import android.graphics.Color;
import android.content.Context;

public class CustomView extends View {
    Paint paint = new Paint();

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.RED);
    }
    @Override
    public void onDraw(Canvas canvas) {
        // Use this format for getting A percent of A screen width/height. This draws downward evenly:
        canvas.drawLine(getLeft(), getHeight() * 0.05f, getRight(), getHeight() * 0.05f, paint);
        canvas.drawLine(getLeft(), getHeight() * 0.1f, getRight(), getHeight() * 0.1f, paint);
        canvas.drawLine(getLeft(), getHeight() * 0.15f, getRight(), getHeight() * 0.15f, paint);
        canvas.drawLine(getLeft(), getHeight() * 0.2f, getRight(), getHeight() * 0.2f, paint);
        canvas.drawLine(getLeft(), getHeight() * 0.25f, getRight(), getHeight() * 0.25f, paint);
        canvas.drawLine(getLeft(), getHeight() * 0.3f, getRight(), getHeight() * 0.3f, paint);
        canvas.drawLine(getLeft(), getHeight() * 0.35f, getRight(), getHeight() * 0.35f, paint);
        canvas.drawLine(getLeft(), getHeight() * 0.4f, getRight(), getHeight() * 0.4f, paint);
        canvas.drawLine(getLeft(), getHeight() * 0.45f, getRight(), getHeight() * 0.45f, paint);
        canvas.drawLine(getLeft(), getHeight() * 0.5f, getRight(), getHeight() * 0.5f, paint);
        canvas.drawLine(getLeft(), getHeight() * 0.55f, getRight(), getHeight() * 0.55f, paint);
        canvas.drawLine(getLeft(), getHeight() * 0.6f, getRight(), getHeight() * 0.6f, paint);
        canvas.drawLine(getLeft(), getHeight() * 0.65f, getRight(), getHeight() * 0.65f, paint);
        canvas.drawLine(getLeft(), getHeight() * 0.7f, getRight(), getHeight() * 0.7f, paint);
        canvas.drawLine(getLeft(), getHeight() * 0.75f, getRight(), getHeight() * 0.75f, paint);
        canvas.drawLine(getLeft(), getHeight() * 0.8f, getRight(), getHeight() * 0.8f, paint);
        canvas.drawLine(getLeft(), getHeight() * 0.85f, getRight(), getHeight() * 0.85f, paint);
        canvas.drawLine(getLeft(), getHeight() * 0.9f, getRight(), getHeight() * 0.9f, paint);
        canvas.drawLine(getLeft(), getHeight() * 0.95f, getRight(), getHeight() * 0.95f, paint);

    }
}
