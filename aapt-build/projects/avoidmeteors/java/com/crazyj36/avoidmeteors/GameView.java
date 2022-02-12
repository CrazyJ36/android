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
    static DisplayMetrics displayMetrics;
    static int x; // static so it can be changed by MainActivity onTouch().
    static int y;
    static float enemyX = 10;
    static float enemyY = 10;
    static int random = 0;
    static int score = 0;
    static int frames = 999999;
    Paint paint = new Paint();
    AlertDialog.Builder dialogBuilder;

    public float dp(float desired) {
        float scale = GameView.this.getResources().getDisplayMetrics().density;
        return desired * scale + 0.5f;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
         // character start.
        displayMetrics = context.getResources().getDisplayMetrics();
        x = displayMetrics.widthPixels / 2; // then do '- percent' for rectagle and out of bounds.
        y = displayMetrics.heightPixels / 2;
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
        paint.setStrokeWidth(dp(4)); // convert px to dp for actual drawing.
        // outline
        canvas.drawRect( (float)2 * displayMetrics.widthPixels / 100, // left screen percentage
                         (float)1 * displayMetrics.heightPixels / 100, // top screen percentage
                         (float)98 * displayMetrics.widthPixels / 100, // right screen percentage
                         dp(displayMetrics.heightPixels * 0.7f) - dp(84), // bottom screen percentage
                         paint);
        // character
        float pts[] = {x, y, x, y - dp(8), // body
                       x, y, x + dp(12), y + dp(16), // right leg
                       x, y, x - dp(12), y + dp(16), // left leg
                       x, y - dp(16), x + dp(18), y - dp(18),  // right arm
                       x, y - dp(16), x - dp(18), y - dp(18)}; // left arm
        canvas.drawLines(pts, paint);
        canvas.drawCircle(x, y - dp(28), dp(8), paint); // head
        // enemy
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        random++;
        enemyY = enemyY + dp(10);
        if (random % 5f == 0) enemyX = enemyX + dp(20);
        else enemyX = enemyX + dp(10);
        if (enemyX >= (97 * displayMetrics.widthPixels / 100)) enemyX = dp(2) * displayMetrics.widthPixels / 100;
        if (enemyY >= displayMetrics.heightPixels * 0.7f - dp(84)) enemyY = dp(1) * displayMetrics.heightPixels / 100;
        canvas.drawCircle(enemyX, enemyY, dp(4), paint);
        // get hit
        if (( (enemyX >= x - dp(16) && enemyX <= x + dp(16)) && (enemyY >= y - dp(32) && enemyY <= y + dp(16)) ) ||
            ( (enemyX + dp(1) >= x - dp(16) && enemyX + dp(1) <= x + dp(16)) && (enemyY + dp(1) >= y - dp(32) && enemyY + dp(1) <= y + dp(16)) ) ||
            ( (enemyX + dp(2) >= x - dp(16) && enemyX + dp(2) <= x + dp(16)) && (enemyY + dp(2) >= y - dp(32) && enemyY + dp(2) <= y + dp(16)) ) ||
            ( (enemyX + dp(3) >= x - dp(16) && enemyX + dp(3) <= x + dp(16)) && (enemyY + dp(3) >= y - dp(32) && enemyY + dp(3) <= y + dp(16)) ) ||
            ( (enemyX + dp(4) >= x - dp(16) && enemyX + dp(4) <= x + dp(16)) && (enemyY + dp(4) >= y - dp(32) && enemyY + dp(4) <= y + dp(16)) ) ||
            ( (enemyX + dp(5) >= x - dp(16) && enemyX + dp(5) <= x + dp(16)) && (enemyY + dp(5) >= y - dp(32) && enemyY + dp(5) <= y + dp(16)) ) ||
            ( (enemyX + dp(6) >= x - dp(16) && enemyX + dp(6) <= x + dp(16)) && (enemyY + dp(6) >= y - dp(32) && enemyY + dp(6) <= y + dp(16)) ) ||
            ( (enemyX + dp(7) >= x - dp(16) && enemyX + dp(7) <= x + dp(16)) && (enemyY + dp(7) >= y - dp(32) && enemyY + dp(7) <= y + dp(16)) ) ||
            ( (enemyX + dp(8) >= x - dp(16) && enemyX + dp(8) <= x + dp(16)) && (enemyY + dp(8) >= y - dp(32) && enemyY + dp(8) <= y + dp(16)) ) ) {
            GameView.this.setWillNotDraw(true);
            MainActivity.scoreView.setText("");
		    dialogBuilder.setTitle("You're Hit!").setMessage("Score: " + String.valueOf(score)).setCancelable(false);
            dialogBuilder.create().show();
        }
        // out of bounds
        if ((x + dp(12)) >= (97 * displayMetrics.widthPixels / 100) || (x - dp(17)) <= (2 * displayMetrics.widthPixels / 100) ||
                (y + dp(19)) >= (displayMetrics.heightPixels * 0.7f - dp(84)) || (y - dp(38)) <= (1 * displayMetrics.heightPixels / 100)) {
            GameView.this.setWillNotDraw(true);
            MainActivity.scoreView.setText("");
            dialogBuilder.setTitle("Out of Bounds!").setMessage("Try again.").setCancelable(false);
            dialogBuilder.create().show();
        }
        // refresh screen for new frames
        invalidate();
    }

}

