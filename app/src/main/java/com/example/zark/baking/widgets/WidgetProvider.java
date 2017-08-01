package com.example.zark.baking.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.zark.baking.R;
import com.example.zark.baking.models.Recipe;
import com.google.gson.Gson;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    private static final String TAG = WidgetProvider.class.getSimpleName();
    private static final String KEY_SELECTED_RECIPE = "selectedRecipe";
    private int[] mAppWidgetIds;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        mAppWidgetIds = appWidgetIds;
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews widget = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);

            // Update title on widget to match recipe title
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String recipeString = preferences.getString(KEY_SELECTED_RECIPE, null);
            if (recipeString != null) {
                Recipe recipe = gson.fromJson(recipeString, Recipe.class);
                widget.setTextViewText(R.id.widget_title, recipe.getName());

            }

            Intent serviceIntent = new Intent(context, WidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);


            widget.setRemoteAdapter(appWidgetId, R.id.widget_list, serviceIntent);

            appWidgetManager.updateAppWidget(appWidgetId, widget);
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

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mAppWidgetIds != null) {
            onUpdate(context, AppWidgetManager.getInstance(context), mAppWidgetIds);
        }
        super.onReceive(context, intent);
    }
}

