package com.crazyj36.apiplayground;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.widget.RemoteViews;

public class AppWidget extends android.appwidget.AppWidgetProvider {
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // update widget view
        appWidgetManager.updateAppWidget(appWidgetIds, new RemoteViews(context.getPackageName(), R.layout.appwidget));
    }
}

