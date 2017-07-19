package com.example.zark.baking;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zark.baking.adapters.DirectionsAdapter;
import com.example.zark.baking.adapters.IngredientsAdapter;
import com.example.zark.baking.models.Recipe;
import com.example.zark.baking.utilities.RecipeBus;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;



/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeOverviewFragment extends Fragment implements DirectionsAdapter.MyDirectionsClickListener {

    public static Bus sRecipeBus;

    private static final String TAG = RecipeOverviewFragment.class.getSimpleName();
    private static final String TAG_RECIPE_OBJECT = "Recipe";

    private Recipe mCurrentRecipe;
    private IngredientsAdapter mIngredientsAdapter;
    private DirectionsAdapter mDirectionsAdapter;
    OnStepClickedListener mCallback;

    public interface OnStepClickedListener {
        public void OnStepClicked(int stepNumber);
    }

    @Override
    public void handleClick(int stepNumber) {
        mCallback.OnStepClicked(stepNumber);
    }

    public RecipeOverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_overview, container, false);


        setupIngredientsList(view);
        setupDirectionsList(view);

        sRecipeBus = RecipeBus.getBus();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentRecipe = (Recipe) savedInstanceState.getSerializable(TAG_RECIPE_OBJECT);
            giveRecipeToAdapters();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnStepClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickedListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        sRecipeBus.register(this);
    }

    private void setupIngredientsList(View view) {
        mIngredientsAdapter = new IngredientsAdapter(getContext(), null);
        RecyclerView ingredientsRecyclerView = view.findViewById(R.id.ingredients_recycler_view);
        ingredientsRecyclerView.setHasFixedSize(true);
        // Scrolling is handled by the NestedScrollView in the main fragment layout
        RecyclerView.LayoutManager ingredientsLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        ingredientsRecyclerView.setLayoutManager(ingredientsLayoutManager);
        ingredientsRecyclerView.setAdapter(mIngredientsAdapter);
    }

    private void setupDirectionsList(View view) {
        mDirectionsAdapter = new DirectionsAdapter(getContext(), null, this);
        RecyclerView directionsRecyclerView = view.findViewById(R.id.directions_recycler_view);
        directionsRecyclerView.setHasFixedSize(true);
        // Scrolling is handled by the NestedScrollView in the main fragment layout
        RecyclerView.LayoutManager directionsLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        directionsRecyclerView.setLayoutManager(directionsLayoutManager);
        directionsRecyclerView.setAdapter(mDirectionsAdapter);
    }

    @Subscribe
    public void getRecipeObjectFromAdapter(Recipe selectedRecipe) {
        mCurrentRecipe = selectedRecipe;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCurrentRecipe != null) {
            giveRecipeToAdapters();
        }
    }

    public void giveRecipeToAdapters() {
        if (mIngredientsAdapter != null) {
            mIngredientsAdapter.setNewData(mCurrentRecipe);
        }

        if (mDirectionsAdapter != null) {
            mDirectionsAdapter.setNewRecipe(mCurrentRecipe);
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
