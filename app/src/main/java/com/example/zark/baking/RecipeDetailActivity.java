package com.example.zark.baking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.zark.baking.models.Recipe;
import com.example.zark.baking.models.Step;
import com.example.zark.baking.utilities.RecipeBus;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;

import java.util.List;


public class RecipeDetailActivity extends AppCompatActivity implements RecipeOverviewFragment.OnStepClickedListener{

    public static Bus sRecipeBus;

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private static final String KEY_RECIPE_OVERVIEW_FRAGMENT = "RecipeOverviewFragment";
    private static final String KEY_STEP_NUMBER = "stepNumber";
    private static final String KEY_SELECTED_RECIPE = "selectedRecipe";
    private static final String KEY_SELECTED_STEP = "selectedStep";
    private RecipeOverviewFragment mRecipeOverviewFragment;
    private View mDetailsPane;
    private boolean mDualPane = false;
    private Recipe mCurrentRecipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Event Bus for sending Recipe objects to fragments
        sRecipeBus = RecipeBus.getBus();

        // If this view container exists, which only exists in the sw900dp version of
        // activity_recipe_detail.xml, then dual pane mode will be enabled
        mDetailsPane = findViewById(R.id.detail_container);
        if (mDetailsPane != null) {
            mDualPane = true;
        }

        if (savedInstanceState == null) {
            // Get the Recipe object from the intent and place is on the Otto event bus
            if (getIntent().getExtras() != null) {
                mCurrentRecipe = getIntent().getParcelableExtra(KEY_SELECTED_RECIPE);
                displayRecipeOverview();
                if (mDualPane) {
                    displayStepDetail(0);
                }
            }
        } else {
            mCurrentRecipe = savedInstanceState.getParcelable(KEY_SELECTED_RECIPE);
        }

        getSupportActionBar().setTitle(mCurrentRecipe.getName());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_SELECTED_RECIPE, mCurrentRecipe);
    }

    public void displayRecipeOverview() {
        if (mRecipeOverviewFragment == null) {
            mRecipeOverviewFragment = new RecipeOverviewFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, mRecipeOverviewFragment).commit();
    }

    /**
     * Callback from RecipeOverviewFragment
     *
     * @param stepNumber the step that the user selected
     */
    @Override
    public void OnStepClicked(int stepNumber) {
        displayStepDetail(stepNumber);
    }

    public void displayStepDetail(int stepNumber) {
        Bundle args = new Bundle();
        args.putInt(KEY_STEP_NUMBER, stepNumber);
        args.putParcelable(KEY_SELECTED_STEP, getCurrentStep(stepNumber));
        StepDetailFragment detailFragment = new StepDetailFragment();
        detailFragment.setArguments(args);

        if (mDualPane) {
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, detailFragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_container,
                    detailFragment).commit();
        }
    }

    /**
     * Returns a Step object of the given step number
     *
     * @param stepNumber number of the desired step
     * @return Step object of the given step number
     */
    public Step getCurrentStep(int stepNumber) {
        List<Step> stepsList = mCurrentRecipe.getSteps();
        Step currentStep = stepsList.get(stepNumber);
        return currentStep;
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
     * Produces the Recipe object so that the recipe detail fragments (which have not yet been
     * created) can receive the Recipe object when they subscribe to the Otto event bus.
     */
    @Produce
    public Recipe produceRecipeForRecipeDetailActivity() {
        Log.v(TAG, "Producing recipe: " + mCurrentRecipe.getName());
        return mCurrentRecipe;
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
