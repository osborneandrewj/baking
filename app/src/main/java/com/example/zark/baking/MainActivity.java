package com.example.zark.baking;

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
 *
 */

public class MainActivity extends AppCompatActivity
        implements RecipeCardsFragment.OnRecipeSelectionListener,
        RecipeOverviewFragment.OnStepClickedListener {

    public static Bus sRecipeBus;


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String KEY_RECIPE_OVERVIEW_FRAGMENT = "RecipeOverviewFragment";
    private static final String KEY_RECIPE_CARDS_FRAGMENT = "RecipeCardsFragment";
    private static final String KEY_STEP_NUMBER = "stepNumber";

    private Recipe mSelectedRecipe;
    private RecipeCardsFragment mRecipeCardsFragment;
    private RecipeOverviewFragment mRecipeOverviewFragment;
    private View mDetailsPane;
    private boolean mDualPane = false;
    private boolean mTabletMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If this view exists, which only exists in the sw960dp version of activity_mail.xml,
        // then dual pane mode will be enabled
        mDetailsPane = findViewById(R.id.detail_container);
        if (mDetailsPane != null) {
            Log.v(TAG, "Dual pane enabled");
            mDualPane = true;
            hideDetailsPane();
        }

        int screenSize = getResources().getConfiguration().screenWidthDp;
        Log.v(TAG, "Screen width = " + screenSize + " " + mDetailsPane);
        Log.v(TAG, "Smallest width = " + getResources().getConfiguration().smallestScreenWidthDp);

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

    private void showDetailsPane() {
        mDetailsPane.setVisibility(View.GONE);
    }

    private void hideDetailsPane() {
        mDetailsPane.setVisibility(View.VISIBLE);
    }

    public void displayRecipeCardFragment() {
        if (mDualPane) {
            hideDetailsPane();
        }
        getSupportFragmentManager().beginTransaction().replace(
                R.id.frag_container, mRecipeCardsFragment).commit();
    }

    /**
     * From RecipeCardsFragment
     */
    @Override
    public void onRecipeSelection() {
        // If dual pane is enabled, display the first step in the detail pane
        if (mDualPane) {
            showDetailsPane();
            displayStepDetailFragment(0);
        }

        displayRecipeOverviewFragment();
    }

    public void displayRecipeOverviewFragment() {
        if (mRecipeOverviewFragment == null) {
            mRecipeOverviewFragment = new RecipeOverviewFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(
                R.id.frag_container, mRecipeOverviewFragment).addToBackStack(null).commit();
    }

    /**
     * From RecipeOverviewFragment
     *
     * @param stepNumber the step that the user selected
     */
    @Override
    public void OnStepClicked(int stepNumber) {
        displayStepDetailFragment(stepNumber);
    }

    public void displayStepDetailFragment(int stepNumber) {
        Bundle args = new Bundle();
        args.putInt(KEY_STEP_NUMBER, stepNumber);

        StepDetailFragment detailFragment = new StepDetailFragment();
        detailFragment.setArguments(args);

        // if dual pane is enabled, open this StepDetailFragment in the detail pane
        if (mDualPane) {
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(
                    R.id.detail_container,
                    detailFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frag_container,
                    detailFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Receives the Recipe object corresponding to the user-selected recipe CardView. We need to
     * get this Recipe object as it needs to be produced in this activity.
     */
    @Subscribe
    public void getRecipeObjectFromAdapter(Recipe selectedRecipe) {
        mSelectedRecipe = selectedRecipe;
    }

    /**
     * Produces the Recipe object so that the recipe detail fragments (which have not yet been
     * created) can receive the Recipe object when they subscribe to the Otto event bus.
     */
    @Produce
    public Recipe produceRecipeForRecipeDetailActivity() {
        return mSelectedRecipe;
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
