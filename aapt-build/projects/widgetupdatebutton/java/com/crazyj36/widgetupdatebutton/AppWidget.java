package com.crazyj36.widgetupdatebutton;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import android.content.Intent;
import android.app.PendingIntent;
import android.widget.Toast;

public class AppWidget extends AppWidgetProvider {

    // A reference number to increment per update button click.
    static int updater = 0;

    @Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) { // Syntax: (type variable arrayToGoThrough)
            // increment number on ever update. Updates include code from myUpdateAppWidget() and onUpdate().
            updater = updater + 1;
            // Using above method here. Runs code from myUpdateAppWidget() before the Toast below.
            myUpdateAppWidget(context, appWidgetManager, appWidgetId);

            // context is carried through the methods in this file, so even toast works.
            Toast.makeText(context, "AppWidget and onUpdate() called.", Toast.LENGTH_SHORT).show();

        }
	}

    // defined widgets update method ourselfs.
    static void myUpdateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

       // Set widget content
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget);
        views.setTextViewText(R.id.tvWidget, String.valueOf(updater));

        // intent for widget to update itself.
        Intent intentUpdate = new Intent(context, AppWidget.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        // Put the widget ids into the self update intent as extra.
        int[] idArray = new int[]{appWidgetId};
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);

        // set self update intent to be called when button is clicked using A pending intent on the button.
        PendingIntent pendingUpdate = PendingIntent.getBroadcast(context, appWidgetId, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.btnWidget, pendingUpdate);

        // regular widget refresh
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

}
