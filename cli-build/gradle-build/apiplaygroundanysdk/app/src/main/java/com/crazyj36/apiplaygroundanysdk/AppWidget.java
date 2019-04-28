package com.crazyj36.apiplaygroundanysdk;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import android.view.View;
import android.os.Build.VERSION;
import android.widget.Toast;

public class AppWidget extends AppWidgetProvider {

	public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
		if (VERSION.SDK_INT >= 3) {
            final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget);
			remoteViews.setTextViewText(R.id.tvWidget, "widget content text");
	        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
		} else {
			Toast.makeText(context.getApplicationContext(), "AppWidgetProvider not available in API 3. I should workaround it if I ever get A device that old", Toast.LENGTH_SHORT).show();
		}
	}
}
