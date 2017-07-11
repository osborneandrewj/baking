package com.example.zark.baking.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zark.baking.R;
import com.example.zark.baking.models.Recipe;

import java.util.List;

/**
 * Created by Andrew Osborne on 7/10/17.
 *
 */

public class RecipeCardAdapter
        extends RecyclerView.Adapter<RecipeCardAdapter.RecipeCardAdapterViewHolder> {

    private static final String TAG = RecipeCardAdapter.class.getSimpleName();

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Recipe> mRecipeList;
    private Recipe mCurrentRecipe;


    public RecipeCardAdapter(Context context, List<Recipe> aRecipeList) {
        mLayoutInflater = LayoutInflater.from(context);
        mRecipeList = aRecipeList;
    }

    @Override
    public RecipeCardAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = mLayoutInflater.inflate(R.layout.recipe_card, parent, false);
        final RecipeCardAdapterViewHolder viewHolder = new RecipeCardAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeCardAdapterViewHolder holder, final int position) {

        // Get current recipe
        mCurrentRecipe = mRecipeList.get(position);
        holder.mRecipeTitleTextView.setText(mCurrentRecipe.getName());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "CardView here! " + mRecipeList.get(position).getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mRecipeList != null) {
            return mRecipeList.size();
        }

        return 0;
    }


    public void setNewRecipeList(List<Recipe> aNewList) {
        mRecipeList = aNewList;
        notifyDataSetChanged();
    }

    /**
     * Inner class to store and recycle views as they are scrolled off screen
     */
    public class RecipeCardAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView mRecipeTitleTextView;
        public CardView mCardView;

        public RecipeCardAdapterViewHolder(View view) {
            super(view);

            // Enable click handling
            mCardView = view.findViewById(R.id.cardview_item);

            // Set data
            mRecipeTitleTextView = (TextView) view.findViewById(R.id.tv_recipie_title);
        }
    }
}
