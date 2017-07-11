package com.example.zark.baking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        RecipeOverviewFragment overviewFragment = new RecipeOverviewFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.recipe_overview_frag_container,
                overviewFragment).commit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }
}
