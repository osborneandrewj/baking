package com.example.zark.baking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.example.zark.baking.adapters.RecipeCardAdapter;
import com.example.zark.baking.models.Recipe;
import com.example.zark.baking.retrofit.RecipeDbApi;
import com.example.zark.baking.retrofit.RecipeDbApiClient;
import com.example.zark.baking.utilities.MyNetworkUtils;
import com.example.zark.baking.utilities.RecipeBus;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * desiree
 */

public class MainActivity extends AppCompatActivity
        implements RecipeCardAdapter.RecipeCardAdapterOnClickHandler {

    public static Bus sRecipeBus;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int ONE_CARD_WIDE = 1;


    private RecyclerView mRecyclerView;
    private RecipeCardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutmanager;
    private Recipe mSelectedRecipe;

    private RecipeDbApi mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recipe_cards_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        // Select gridlayout size based on screen width and orientation
        mLayoutmanager = new GridLayoutManager(this, 1);

        mAdapter = new RecipeCardAdapter(this, new ArrayList<Recipe>(), this);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutmanager);

        // Event Bus for sending Recipe objects
        sRecipeBus = RecipeBus.getBus();

        getRecipeDataFromUdacity();
    }

    /**
     * Use retrofit to retrieve recipe data from the Udacity server
     */
    public void getRecipeDataFromUdacity() {
        if (mService == null) {
            mService = RecipeDbApiClient.getClient().create(RecipeDbApi.class);
        }

        // Check for available network connection
        if (!MyNetworkUtils.doesNetworkConnectionExist(this)) {
            showEmptyState();
            return;
        }

        Call<List<Recipe>> callRecipes = mService.getRecipeList();
        callRecipes.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                hideEmptyState();

                List<Recipe> recipeList = response.body();
                mAdapter.setNewRecipeList(recipeList);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.v(TAG, "Shoot, nothing here.");
                t.printStackTrace();
                showEmptyState();
            }
        });
    }

    public void showEmptyState() {
        //TODO: fill in
    }

    public void hideEmptyState() {
        //TODO: fill in
    }

    @Override
    public void onRecipeCardClick() {
        Intent startRecipeDetailActivityIntent = new Intent(
                this, RecipeDetailActivity.class);
        startActivity(startRecipeDetailActivityIntent);
    }

    @Subscribe
    public void getRecipeObjectFromAdapter(Recipe selectedRecipe) {
        mSelectedRecipe = selectedRecipe;
    }

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
