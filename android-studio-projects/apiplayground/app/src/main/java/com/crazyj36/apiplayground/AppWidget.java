package com.crazyj36.apiplayground;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class AppWidget extends AppWidgetProvider {
    public void onUpdate(final Context onUpdateContext, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {

        // Get the xml layout for the Widget and it's ids.
        RemoteViews widgetLayoutView;
        widgetLayoutView = new RemoteViews(onUpdateContext.getPackageName(), R.layout.appwidget);

        // Set Widget text in code
        widgetLayoutView.setTextViewText(R.id.widgetTv, onUpdateContext.getResources().getText(R.string.widgetTvTxt));

        // Widget button
        Intent startMainIntent = new Intent(onUpdateContext, MainActivity.class);
        PendingIntent pendingStartMain = PendingIntent.getActivity(onUpdateContext, 0, startMainIntent, 0);
        widgetLayoutView.setOnClickPendingIntent(R.id.widgetBtn, pendingStartMain);

        // Do refresh of widget
        appWidgetManager.updateAppWidget(appWidgetIds, widgetLayoutView);
    }

}

