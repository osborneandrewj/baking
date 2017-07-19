package com.example.zark.baking;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private static final String KEY_STEP_NUMBER = "stepNumber";
    private static final String KEY_CURRENT_FRAGMENT = "currentFragment";

    private Recipe mSelectedRecipe;
    private RecipeCardsFragment mRecipeCardsFragment;
    private RecipeOverviewFragment mRecipeOverviewFragment;
    private Fragment mCurrentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Event Bus for sending Recipe objects
        sRecipeBus = RecipeBus.getBus();

        if (savedInstanceState != null) {
            mCurrentFragment = getSupportFragmentManager().getFragment(
                    savedInstanceState,
                    KEY_CURRENT_FRAGMENT);
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,
                    mCurrentFragment).commit();
        } else {
            displayRecipeCardFragment();
        }
    }

    public void displayRecipeCardFragment() {
        if (mRecipeCardsFragment == null) {
            mRecipeCardsFragment = new RecipeCardsFragment();
        }

        mCurrentFragment = mRecipeCardsFragment;

        getSupportFragmentManager().beginTransaction().replace(
                R.id.frag_container, mRecipeCardsFragment).commit();
    }

    /**
     * From RecipeCardsFragment
     */
    @Override
    public void onRecipeSelection() {
        displayRecipeOverviewFragment();
    }

    public void displayRecipeOverviewFragment() {
        if (mRecipeOverviewFragment == null) {
            mRecipeOverviewFragment = new RecipeOverviewFragment();
        }

        mCurrentFragment = mRecipeOverviewFragment;

        getSupportFragmentManager().beginTransaction().replace(
                R.id.frag_container, mRecipeOverviewFragment).commit();
    }

    /**
     * From RecipeOverviewFragment
     *
     * @param stepNumber the step that the user selected
     *
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

        mCurrentFragment = detailFragment;

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,
                detailFragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current fragment's instance
        getSupportFragmentManager().putFragment(
                outState, KEY_CURRENT_FRAGMENT, mCurrentFragment);
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
