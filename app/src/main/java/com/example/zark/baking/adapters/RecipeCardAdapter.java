package com.example.zark.baking.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zark.baking.models.Recipe;

import java.util.List;

/**
 * Created by Andrew Osborne on 7/10/17.
 *
 */

public class RecipeCardAdapter
        extends RecyclerView.Adapter<RecipeCardAdapter.RecipeCardAdapterViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Recipe> mRecipeList;

    public RecipeCardAdapter(Context context, List<Recipe> aRecipeList) {
        mLayoutInflater = LayoutInflater.from(context);
        mRecipeList = aRecipeList;
    }

    @Override
    public RecipeCardAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecipeCardAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (mRecipeList != null) {
            return mRecipeList.size();
        }

        return 0;
    }

    /**
     * Inner class to store and recycle views as they are scrolled off screen
     */
    public class RecipeCardAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView mRecipeTitleTextView;

        public RecipeCardAdapterViewHolder(View view) {
            super(view);
        }

    }
}
