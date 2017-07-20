package com.example.zark.baking;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zark.baking.models.Recipe;
import com.example.zark.baking.models.Step;
import com.example.zark.baking.utilities.RecipeBus;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment {

    public static Bus sRecipeBus;

    private static final String TAG_RECIPE_OBJECT = "Recipe";
    private static final String KEY_STEP_NUMBER = "stepNumber";


    private Recipe mCurrentRecipe;
    private TextView mStepDescriptionTextView;
    private int mStepNumber;


    public StepDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_step_detail, container, false);

        mStepDescriptionTextView = view.findViewById(R.id.tv_step_description);

        sRecipeBus = RecipeBus.getBus();

        if (getArguments() != null) {
            mStepNumber = getArguments().getInt(KEY_STEP_NUMBER, -1);
            Log.v("detail", "step number: " + mStepNumber);
        }


        if (mCurrentRecipe != null) {
            Log.v("detail", "recipe is not null!");
            List<Step> stepsList = mCurrentRecipe.getSteps();
            mStepDescriptionTextView.setText(stepsList.get(mStepNumber).getDescription());
        } else {

        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentRecipe = (Recipe) savedInstanceState.getSerializable(TAG_RECIPE_OBJECT);
            Log.v("detail", "what");
            List<Step> stepsList = mCurrentRecipe.getSteps();
            mStepDescriptionTextView.setText(stepsList.get(mStepNumber).getDescription());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        sRecipeBus.register(this);
    }

    @Subscribe
    public void getRecipeObjectFromAdapter(Recipe selectedRecipe) {
        mCurrentRecipe = selectedRecipe;
        Log.v("detail", "got a recipe" + selectedRecipe.getName());
        setRecipeData();
    }

    public void setRecipeData() {
        if (mCurrentRecipe != null) {
            Log.v("detail", "recipe is not null!");
            List<Step> stepsList = mCurrentRecipe.getSteps();
            mStepDescriptionTextView.setText(stepsList.get(mStepNumber).getDescription());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(TAG_RECIPE_OBJECT, mCurrentRecipe);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (sRecipeBus != null) {
            sRecipeBus.unregister(this);
        }
    }

}
