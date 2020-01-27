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

    int updater = 0;

	public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        if (VERSION.SDK_INT >= 3) {
            final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget);

            remoteViews.setTextViewText(R.id.tvWidget, String.valueOf(updater + 1));

            Intent startMainIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingStartMain = PendingIntent.getActivity(context, 0, startMainIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widgetBtn, pendingStartMain);

            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

	    } else {
		    Toast.makeText(context.getApplicationContext(), "AppWidgetProvider not available below Android API level 3.", Toast.LENGTH_SHORT).show();
	    }

	}
}
