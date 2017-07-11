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
    private IngredientsAdapter mIngredientsAdapter;
    private RecyclerView.LayoutManager mIngredientsLayoutManager;

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
        mIngredientsRecyclerView = (RecyclerView) view.findViewById(R.id.ingredients_recycler_view);
        mIngredientsRecyclerView.setHasFixedSize(true);
        mIngredientsLayoutManager = new LinearLayoutManager(getContext());

        mIngredientsRecyclerView.setLayoutManager(mIngredientsLayoutManager);
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);

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
        String message = mCurrentRecipe.getName();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void giveRecipeToAdapters() {
        if (mIngredientsAdapter != null) {
            mIngredientsAdapter.setNewRecipe(mCurrentRecipe);
            Log.v("TAG", "count: " + mIngredientsAdapter.getItemCount());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        RecipeBus.getBus().unregister(this);
    }
}
