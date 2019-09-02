package com.crazyj36.apiplayground;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


public class AppWidget extends AppWidgetProvider {



    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget);

        // A Widget Button
        // Make an pending intent for button(go to mainactivity
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        // Get the xml layout for the Widget and attach A click listener to button to start pending intent from above.
        RemoteViews widgetLayoutView = new RemoteViews(context.getPackageName(), R.layout.appwidget);
        widgetLayoutView.setOnClickPendingIntent(R.id.widgetBtn, pendingIntent);

        // Set Widget text in code
        // try a loop change of text, then updateWidget
        remoteViews.setTextViewText(R.id.widgetTv, context.getResources().getText(R.string.widgetTvTxt));

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

    }
}

