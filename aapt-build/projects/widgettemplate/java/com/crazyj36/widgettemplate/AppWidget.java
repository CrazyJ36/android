package com.crazyj36.widgettemplate;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public class AppWidget extends AppWidgetProvider {

	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Set widget content
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget);
        remoteViews.setTextViewText(R.id.tvWidget, "Widget text, set in Java");
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}
}
