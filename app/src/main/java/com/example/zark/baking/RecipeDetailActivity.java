package com.example.zark.baking;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
    private int mCurrentStepDisplayed;
    private TextView mButtonNext;
    private TextView mButtonBack;
    private TextView mEmptyState;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Event Bus for sending Recipe objects to fragments
        sRecipeBus = RecipeBus.getBus();

        // If this view container exists, which only exists in the sw900dp version of
        // activity_recipe_detail.xml, then dual pane mode will be enabled
        mDetailsPane = findViewById(R.id.detail_container);
        if (mDetailsPane != null) {
            mDualPane = true;
        }

        if (!mDualPane) {
            // Navigation buttons for phone
            mButtonNext = (TextView) findViewById(R.id.button_next);
            mButtonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    displayStepDetail(mCurrentStepDisplayed + 1);
                }
            });
            mButtonBack = (TextView) findViewById(R.id.button_back);
            mButtonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    displayStepDetail(mCurrentStepDisplayed - 1);
                }
            });
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
            // If there is nothing in the back stack then the user is currently looking at
            // the recipe overview, in which case we do not want the navigation buttons
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                hideNavigationButtons();
            }
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

        hideNavigationButtons();
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

        mCurrentStepDisplayed = stepNumber;

        // Error checking
        if (stepNumber < 0) {
            mCurrentStepDisplayed = 0;
        }
        List<Step> stepsList = mCurrentRecipe.getSteps();
        if (stepNumber > stepsList.size() - 1) {
            mCurrentStepDisplayed = stepsList.size() - 1;
        }

        Bundle args = new Bundle();
        args.putParcelable(KEY_SELECTED_STEP, getCurrentStep(mCurrentStepDisplayed));
        StepDetailFragment detailFragment = new StepDetailFragment();
        detailFragment.setArguments(args);

        if (mDualPane) {
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, detailFragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_container,
                    detailFragment).commit();
        }

        showNavigationButtons();
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

    public void showNavigationButtons() {
        if (mDualPane) {
            return;
        }
        mButtonNext.setVisibility(View.VISIBLE);
        mButtonBack.setVisibility(View.VISIBLE);
    }

    public void hideNavigationButtons() {
        if (mDualPane) {
            return;
        }
        mButtonNext.setVisibility(View.GONE);
        mButtonBack.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        // If dual pane mode is enabled we want to user to navigate back to the previous activity
        if (mDualPane) {
            super.onBackPressed();
        }
        // If the user is viewing a step detail, we want them to navigate back to the recipe
        // overview (index of 0 in the back stack)
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            hideNavigationButtons();
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
