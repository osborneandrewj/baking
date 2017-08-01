package com.example.zark.baking.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.zark.baking.R;
import com.example.zark.baking.models.Ingredient;
import com.example.zark.baking.models.Recipe;
import com.example.zark.baking.utilities.MyNumberUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew Osborne on 7/29/2017.
 *
 */

public class IngredientsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = IngredientsRemoteViewsFactory.class.getSimpleName();
    private static final String KEY_SELECTED_RECIPE = "selectedRecipe";
    private Recipe mRecipe;
    private List<Ingredient> mIngredientsList = new ArrayList<>();
    private Context mContext;
    private int mAppWidgetId;
    private Intent mIntent;

    /**
     * Public constructor
     */
    public IngredientsRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mIntent = intent;
        mAppWidgetId = mIntent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String recipeString = preferences.getString(KEY_SELECTED_RECIPE, null);
        if (recipeString != null) {
            mRecipe = gson.fromJson(recipeString, Recipe.class);
            mIngredientsList = mRecipe.getIngredients();
            Log.v(TAG, "Sending a recipe to the widget again! " + mRecipe.getName());
        }
    }

    @Override
    public void onDestroy() {
        mIngredientsList.clear();
    }

    @Override
    public int getCount() {
        if (mIngredientsList == null || mIngredientsList.isEmpty()) {
            Log.v(TAG, "getCount: " + 0);
            return 0;
        } else {
            Log.v(TAG, "getCount: " + mIngredientsList.size());
            return mIngredientsList.size();
        }
    }

    /**
     * Just like the getView() of the Adapter class
     */
    @Override
    public RemoteViews getViewAt(int i) {
        final RemoteViews row = new RemoteViews(
                mContext.getPackageName(), R.layout.ingredient_item);

        // Get the Ingredient at the selected position
        Ingredient ingredient = mIngredientsList.get(i);
        // Set the data from this Ingredient to the corresponding lines in the listview
        row.setTextViewText(R.id.tv_ingredient_quantity,
                MyNumberUtils.formatQuantityToString(ingredient.getQuantity()));
        row.setTextViewText(R.id.tv_ingredient_unit,
                ingredient.getMeasure());
        row.setTextViewText(R.id.tv_ingredient_material,
                ingredient.getIngredient());
        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
