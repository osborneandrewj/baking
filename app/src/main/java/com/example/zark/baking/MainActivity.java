package com.example.zark.baking;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.zark.baking.models.Recipe;
import com.example.zark.baking.utilities.RecipeBus;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

/**
 * Created by Andrew Osborne 2017
 *
 * Food thumbnail Created by Onlyyouqj - Freepik.com
 */

public class MainActivity extends AppCompatActivity
        implements RecipeCardsFragment.OnRecipeSelectionListener {

    public static Bus sRecipeBus;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String KEY_RECIPE_CARDS_FRAGMENT = "RecipeCardsFragment";
    private static final String KEY_SELECTED_RECIPE = "selectedRecipe";

    private Recipe mSelectedRecipe;
    private RecipeCardsFragment mRecipeCardsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // Event Bus for sending Recipe objects
        sRecipeBus = RecipeBus.getBus();

        if (savedInstanceState == null) {
            mRecipeCardsFragment = new RecipeCardsFragment();
            displayRecipeCardFragment();
        } else {
            mRecipeCardsFragment = (RecipeCardsFragment) getSupportFragmentManager()
                    .findFragmentByTag(KEY_RECIPE_CARDS_FRAGMENT);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void displayRecipeCardFragment() {

        getSupportFragmentManager().beginTransaction().replace(
                R.id.frag_container, mRecipeCardsFragment).commit();
    }

    /**
     * From RecipeCardsFragment
     */
    @Override
    public void onRecipeSelection() {
        Intent launchDetailActivityIntent = new Intent(this, RecipeDetailActivity.class);
        launchDetailActivityIntent.putExtra(KEY_SELECTED_RECIPE, mSelectedRecipe);
        Log.v(TAG, "Launching activity: " + mSelectedRecipe.getName());
        startActivity(launchDetailActivityIntent);

    }

    /**
     * Receives the Recipe object corresponding to the user-selected recipe CardView. We need to
     * get this Recipe object as it needs to be produced in this activity.
     */
    @Subscribe
    public void getRecipeObjectFromAdapter(Recipe selectedRecipe) {
        Log.v(TAG, "Got something: " + selectedRecipe.getName());
        mSelectedRecipe = selectedRecipe;
    }

    @Override
    protected void onStart() {
        super.onStart();
        sRecipeBus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sRecipeBus != null) {
            sRecipeBus.unregister(this);
        }
    }
}
