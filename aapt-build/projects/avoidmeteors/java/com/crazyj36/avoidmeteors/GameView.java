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

public class GameView extends View {
    DisplayMetrics displayMetrics;
    static int x; // static so it can be changed by MainActivity onTouch().
    static int y;
    int enemyX = 10;
    int enemyY = 10;
    int random = 0;
    int score = 0;
    static int frames = 999999;
    Paint paint = new Paint();
    AlertDialog.Builder dialogBuilder;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        displayMetrics = context.getApplicationContext().getResources().getDisplayMetrics();
	    x = displayMetrics.widthPixels / 2;
	    y = displayMetrics.heightPixels / 2;
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
        score = score + 1;
        if (frames > 0) frames = frames - 10; // lower the sleep nanoseconds.
        MainActivity.scoreView.setText(GameView.this.getResources().getString(R.string.scoreText) + String.valueOf(score));
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
            MainActivity.scoreView.setText("");
		    dialogBuilder.setTitle("You're Hit!").setMessage("Score: " + String.valueOf(score)).setCancelable(false);
            dialogBuilder.create().show();
        }
        // out of bounds
        if ((x + 12) >= (97 * displayMetrics.widthPixels / 100) || (x - 17) <= (2 * displayMetrics.widthPixels / 100) ||
                (y + 19) >= (displayMetrics.heightPixels * 0.7f) || (y - 38) <= (1 * displayMetrics.heightPixels / 100)) {
            GameView.this.setWillNotDraw(true);
            MainActivity.scoreView.setText("");
            dialogBuilder.setTitle("Out of Bounds!").setMessage("Try again.").setCancelable(false);
            dialogBuilder.create().show();
        }
        // refresh screen for new frames
        invalidate();
    }
}

