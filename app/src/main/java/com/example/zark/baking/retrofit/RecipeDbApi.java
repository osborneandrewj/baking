package com.example.zark.baking.retrofit;

import com.example.zark.baking.models.Recipe;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Andrew Osborne on 7/10/17.
 *
 */

public interface RecipeDbApi {

    // Get a list of recipes
    @GET("baking.json")
    Call<List<Recipe>> getRecipeList();
}
