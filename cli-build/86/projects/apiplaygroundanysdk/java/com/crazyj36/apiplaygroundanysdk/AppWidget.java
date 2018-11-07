package com.crazyj36.apiplaygroundanysdk;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import android.app.PendingIntent;
import android.os.Build.VERSION;
import android.os.Handler;
import java.lang.Thread;

public class AppWidget extends AppWidgetProvider {
	Context context;
	int z = 0;
	String y = "";

	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		if (VERSION.SDK_INT >= 3) {
			final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget);
			Handler handler = new Handler();

			Runnable runThis = new Runnable() {
				@Override
				public void run() {
				}
			};
			while (z < 10) {
				handler.postDelayed(runThis, 500); // use thread.sleep for widget in postdelayed run,that worked for the widget before
				remoteViews.setTextViewText(R.id.tvWidget, Integer.toString(z));
				appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
				Toast.makeText(context.getApplicationContext(), Integer.toString(z), Toast.LENGTH_LONG).show();
				z++;
			}


		} else {
			Toast.makeText(context.getApplicationContext(), "AppWidgetProvider not available in API 3. Test, and update app", Toast.LENGTH_SHORT).show();
		}
	}
}
