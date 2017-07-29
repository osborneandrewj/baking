package com.example.zark.baking;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.zark.baking.models.Recipe;
import com.example.zark.baking.retrofit.RecipeDbApi;
import com.example.zark.baking.retrofit.RecipeDbApiClient;
import com.example.zark.baking.utilities.MyNetworkUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implementation of App Widget functionality.
 */
public class AndroidRecipeAppWidget extends AppWidgetProvider {

    private static final String TAG = AndroidRecipeAppWidget.class.getSimpleName();
    private RecipeDbApi mService;
    private List<Recipe> mRecipeList;
    private Context mContext;
    private RemoteViews mRemoteView;



    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        //views.setTextViewText(R.id.tv_recipie_title, widgetText);

        // Instruct the widget manager to update the widget
        //appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            mContext = context;

            mRemoteView = new RemoteViews(context.getPackageName(), R.layout.android_recipe_app_widget);
            getRecipeDataFromUdacity();
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     * Use retrofit to retrieve recipe data from the Udacity server
     */
    public void getRecipeDataFromUdacity() {

        if (mRecipeList != null) {
            //mAdapter.setNewRecipeList(mRecipeList);
            return;
        }

        if (mService == null) {
            mService = RecipeDbApiClient.getClient().create(RecipeDbApi.class);
        }

        // Check for available network connection
        if (!MyNetworkUtils.doesNetworkConnectionExist(mContext)) {
            return;
        }

        Call<List<Recipe>> callRecipes = mService.getRecipeList();
        callRecipes.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {


                Log.v(TAG, "Communicating with server...");

                mRecipeList = response.body();

                Recipe currentRecipe = mRecipeList.get(0);

                mRemoteView.setTextViewText(R.id.tv_widget_title, currentRecipe.getName());
                Log.v(TAG, "Widget title: " + currentRecipe.getName());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.v(TAG, "Shoot, nothing here.");
                t.printStackTrace();
            }
        });
    }
}

