package com.example.zark.baking;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zark.baking.adapters.DirectionsAdapter;
import com.example.zark.baking.adapters.IngredientsAdapter;
import com.example.zark.baking.models.Recipe;
import com.example.zark.baking.utilities.RecipeBus;
import com.squareup.otto.Subscribe;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeOverviewFragment extends Fragment {

    private Recipe mCurrentRecipe;
    private RecyclerView mIngredientsRecyclerView;
    private RecyclerView mDirectionsRecyclerView;
    private IngredientsAdapter mIngredientsAdapter;
    private DirectionsAdapter mDirectionsAdapter;
    private RecyclerView.LayoutManager mIngredientsLayoutManager;
    private RecyclerView.LayoutManager mDirectionsLayoutManager;

    public RecipeOverviewFragment() {
        // Required empty public constructor
    }

    @Override
     public void onStart() {
        super.onStart();
        RecipeBus.getBus().register(this);
    }

    @Subscribe
    public void getRecipeObjectFromAdapter(Recipe selectedRecipe) {
        mCurrentRecipe = selectedRecipe;
        Log.v("RecipeOverviewFragment", "Got the recipe!" + selectedRecipe.getName());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_overview, container, false);
        mIngredientsAdapter = new IngredientsAdapter(getContext(), null);
        mIngredientsRecyclerView = view.findViewById(R.id.ingredients_recycler_view);
        mIngredientsRecyclerView.setHasFixedSize(true);
        mIngredientsLayoutManager = new LinearLayoutManager(getContext());
        mIngredientsRecyclerView.setLayoutManager(mIngredientsLayoutManager);
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);

        mDirectionsAdapter = new DirectionsAdapter(getContext(), null);
        mDirectionsRecyclerView = view.findViewById(R.id.directions_recycler_view);
        mDirectionsRecyclerView.setHasFixedSize(true);
        mDirectionsLayoutManager = new LinearLayoutManager(getContext());
        mDirectionsRecyclerView.setLayoutManager(mDirectionsLayoutManager);
        mDirectionsRecyclerView.setAdapter(mDirectionsAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mCurrentRecipe != null) {
            displayCurrentRecipe();
            giveRecipeToAdapters();
        }
    }

    private void displayCurrentRecipe() {

    }

    private void giveRecipeToAdapters() {
        if (mIngredientsAdapter != null) {
            mIngredientsAdapter.setNewRecipe(mCurrentRecipe);

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        RecipeBus.getBus().unregister(this);
    }
}
