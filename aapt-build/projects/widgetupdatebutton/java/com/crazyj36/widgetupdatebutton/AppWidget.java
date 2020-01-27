package com.crazyj36.widgetupdatebutton;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import android.content.Intent;
import android.app.PendingIntent;

public class AppWidget extends AppWidgetProvider {

    static int updater = 0;

        // Define updateAppWidget ourself
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        updater = updater + 1;
        // Set widget content
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget);
        views.setTextViewText(R.id.tvWidget, String.valueOf(updater));

        // Pending intent for widget to update itself.
        Intent intentUpdate = new Intent(context, AppWidget.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] idArray = new int[]{appWidgetId};
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);
        PendingIntent pendingUpdate = PendingIntent.getBroadcast(context, appWidgetId, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.btnWidget, pendingUpdate);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            // Using above method here
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
	}
}
