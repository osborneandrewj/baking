package com.example.zark.baking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.zark.baking.adapters.RecipeCardAdapter;
import com.example.zark.baking.models.Recipe;
import com.example.zark.baking.retrofit.RecipeDbApi;
import com.example.zark.baking.retrofit.RecipeDbApiClient;
import com.example.zark.baking.utilities.MyNetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int ONE_CARD_WIDE = 1;


    private RecyclerView mRecyclerView;
    private RecipeCardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutmanager;

    private RecipeDbApi mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recipe_cards_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        // Select gridlayout size based on screen width and orientation
        mLayoutmanager = new GridLayoutManager(this, 1);

        mAdapter = new RecipeCardAdapter(this, new ArrayList<Recipe>());

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutmanager);

        getRecipeData();
    }

    /**
     * Use retrofit to retrieve recipe data from the Udacity server
     */
    public void getRecipeData() {
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
}
