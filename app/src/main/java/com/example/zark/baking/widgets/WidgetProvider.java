package com.example.zark.baking.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.zark.baking.R;
import com.example.zark.baking.models.Recipe;
import com.example.zark.baking.retrofit.RecipeDbApi;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    private static final String TAG = WidgetProvider.class.getSimpleName();

    private RemoteViews updateAppWidgetList(Context context, int appWidgetId) {

        Log.v(TAG, "Updating a widget");

        // Layout to show on widget
        RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);

        Intent serviceIntent = new Intent(context, WidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // What does this do?
        serviceIntent.setData(Uri.parse(
                serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteView.setRemoteAdapter(appWidgetId, R.id.widget_list, serviceIntent);
        // TODO: set an empty view
        //remoteView.setEmptyView(R.id.widget_list, R.id.empty_view);
        return remoteView;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            RemoteViews remoteView = updateAppWidgetList(context, appWidgetId);

            appWidgetManager.updateAppWidget(appWidgetId, remoteView);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

