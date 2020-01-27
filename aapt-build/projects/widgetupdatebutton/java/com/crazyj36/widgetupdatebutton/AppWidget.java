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

    // defined widgets update method ourselfs.
    static void myUpdateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // increment number everytime updateAppWidget is called, up to 10
        if (updater == 10) {
            updater = 0;
        }
        updater = updater + 1;

       // Set widget content
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget);
        views.setTextViewText(R.id.tvWidget, "Counts on button click: " + String.valueOf(updater));

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

    @Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) { // Syntax: (type variable arrayToGoThrough)
            // Using above method here
            myUpdateAppWidget(context, appWidgetManager, appWidgetId);

            // context is carried through the methods in this file, so even toast works.
            Toast.makeText(context, "AppWidget onUpdate() called.", Toast.LENGTH_SHORT).show();
        }
	}
}
