package com.crazyj36.apiplayground;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class AppWidget extends AppWidgetProvider {

    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {

        // Get the xml layout for the Widget and it's ids.
        RemoteViews widgetLayoutView;
        widgetLayoutView = new RemoteViews(context.getPackageName(), R.layout.appwidget);

        // Make an pending intent for button(go to mainactivity)
        Intent explicitToastIntent = new Intent(context, com.crazyj36.apiplayground.showToast.class);
        PendingIntent showToastPendingIntent = PendingIntent.getActivity(context, 0, explicitToastIntent, 0 );
        widgetLayoutView.setOnClickPendingIntent(R.id.widgetBtn, showToastPendingIntent);

        // Set Widget text in code
        widgetLayoutView.setTextViewText(R.id.widgetTv, context.getResources().getText(R.string.widgetTvTxt));

        appWidgetManager.updateAppWidget(appWidgetIds, widgetLayoutView);

    }
}

