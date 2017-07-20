package com.example.zark.baking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.zark.baking.models.Recipe;
import com.squareup.otto.Produce;


public class RecipeDetailActivity extends AppCompatActivity implements RecipeOverviewFragment.OnStepClickedListener{

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private static final String KEY_RECIPE_OVERVIEW_FRAGMENT = "RecipeOverviewFragment";
    private static final String KEY_STEP_NUMBER = "stepNumber";
    private static final String KEY_SELECTED_RECIPE = "selectedRecipe";


    private RecipeOverviewFragment mRecipeOverviewFragment;
    private View mDetailsPane;
    private boolean mDualPane = false;
    private boolean mTabletMode = false;
    private Recipe mCurrentRecipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // If this view exists, which only exists in the sw960dp version of activity_mail.xml,
        // then dual pane mode will be enabled
        mDetailsPane = findViewById(R.id.detail_container);
        if (mDetailsPane != null) {
            Log.v(TAG, "Dual pane enabled");
            mDualPane = true;
        }

        if (getIntent().getExtras() != null) {
            mCurrentRecipe = getIntent().getParcelableExtra(KEY_SELECTED_RECIPE);
            String message = mCurrentRecipe.getName();
            Log.v(TAG, "Detail of " + message);
        }

        if (savedInstanceState == null) {

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_SELECTED_RECIPE, mCurrentRecipe);
    }

    private void showDetailsPane() {
        mDetailsPane.setVisibility(View.GONE);
    }

    private void hideDetailsPane() {
        mDetailsPane.setVisibility(View.VISIBLE);
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
     * Produces the Recipe object so that the recipe detail fragments (which have not yet been
     * created) can receive the Recipe object when they subscribe to the Otto event bus.
     */
    @Produce
    public Recipe produceRecipeForRecipeDetailActivity() {
        //return mCurrentRecipe
        return null;
    }
}
