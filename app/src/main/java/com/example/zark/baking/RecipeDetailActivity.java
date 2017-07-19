package com.example.zark.baking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class RecipeDetailActivity extends AppCompatActivity implements RecipeOverviewFragment.OnStepClickedListener{

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private static final String KEY_RECIPE_OVERVIEW_FRAGMENT = "RecipeOverviewFragment";
    private static final String KEY_STEP_NUMBER = "stepNumber";

    private RecipeOverviewFragment mRecipeOverviewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState != null) {
            mRecipeOverviewFragment = (RecipeOverviewFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, KEY_RECIPE_OVERVIEW_FRAGMENT);
        } else {
            mRecipeOverviewFragment = new RecipeOverviewFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.recipe_overview_frag_container,
                mRecipeOverviewFragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the fragment's instance
        getSupportFragmentManager().putFragment(
                outState, KEY_RECIPE_OVERVIEW_FRAGMENT, mRecipeOverviewFragment);
    }

    private void replaceOverviewFragment(int stepNumber) {
        Bundle args = new Bundle();
        args.putInt(KEY_STEP_NUMBER, stepNumber);
        StepDetailFragment detailFragment = new StepDetailFragment();
        detailFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.recipe_overview_frag_container,
                detailFragment).commit();
    }

    @Override
    public void OnStepClicked(int stepNumber) {
        if (getResources().getBoolean(R.bool.tabletMode)) {

        } else {
            replaceOverviewFragment(stepNumber);
        }
    }
}
