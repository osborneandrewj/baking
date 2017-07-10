package com.example.zark.baking;

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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecipeCardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutmanager;
    private static final int ONE_CARD_WIDE = 1;

    private RecipeDbApi mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recipe_cards_recycler_view);

        // Select gridlayout size based on screen width and orientation
        mLayoutmanager = new GridLayoutManager(this, 1);

        mAdapter = new RecipeCardAdapter(this, new ArrayList<Recipe>());

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutmanager);

        getRecipeData();
    }

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

                Log.v(TAG, "Hey, something's here!");
                for (int i=0; i<response.body().size(); i++) {
                    String name = response.body().get(i).getName();
                    Log.v(TAG, "Recipe name: " + name);
                }
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

    }

    public void hideEmptyState() {

    }
}
