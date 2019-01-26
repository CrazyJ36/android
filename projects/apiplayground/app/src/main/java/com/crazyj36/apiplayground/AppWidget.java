package com.crazyj36.apiplayground;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public class AppWidget extends AppWidgetProvider {
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget);
        remoteViews.setTextViewText(R.id.tvWidget, context.getPackageName());

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

    }
}

