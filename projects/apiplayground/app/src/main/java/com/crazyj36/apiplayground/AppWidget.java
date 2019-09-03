package com.crazyj36.apiplayground;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;


public class AppWidget extends AppWidgetProvider {

    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {

        // Get the xml layout for the Widget and it's ids.
        RemoteViews widgetLayoutView;
        widgetLayoutView = new RemoteViews(context.getPackageName(), R.layout.appwidget);

        // Make an pending intent for button(go to mainactivity
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        // Button launching above pending intent
        // Attach A click listener to button to start pending intent from above.
        widgetLayoutView.setOnClickPendingIntent(R.id.widgetBtn, pendingIntent);

        // Set Widget text in code
        widgetLayoutView.setTextViewText(R.id.widgetTv, context.getResources().getText(R.string.widgetTvTxt));

        appWidgetManager.updateAppWidget(appWidgetIds, widgetLayoutView);

    }

}

