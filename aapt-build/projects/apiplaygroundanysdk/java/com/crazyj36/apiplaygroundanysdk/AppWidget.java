package com.crazyj36.apiplaygroundanysdk;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import android.view.View;
import android.os.Build.VERSION;
import android.widget.Toast;
import android.content.Intent;
import android.app.PendingIntent;

public class AppWidget extends AppWidgetProvider {

    static int updater = 0;

    // Overriden method defining the app widget update procudure.
    static void myUpdateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
    if (VERSION.SDK_INT >= 3) {
            updater = updater + 1;
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget);
            remoteViews.setTextViewText(R.id.tvWidget, String.valueOf(updater));

            Intent startMainIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingStartMain = PendingIntent.getActivity(context, 0, startMainIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widgetLaunchBtn, pendingStartMain);

            Intent intentUpdate = new Intent(context, AppWidget.class);;
            intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            int[] idArray = new int[] {appWidgetId};
            intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);

            PendingIntent pendingUpdate = PendingIntent.getBroadcast(context, appWidgetId, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widgetUpdateBtn, pendingUpdate);
            Toast.makeText(context, "myUpdateAppWidget() called.", 0).show();

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

	    } else {
		    Toast.makeText(context, "AppWidgetProvider not available below Android API level 3.", Toast.LENGTH_SHORT).show();
	    }
    }

    // Called when widget updates
    @Override
	public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            myUpdateAppWidget(context, appWidgetManager, appWidgetId);
            Toast.makeText(context, "onUpdate() widget called.", 0).show();
        }
	}

}
