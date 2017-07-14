package com.example.zark.baking.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zark.baking.R;
import com.example.zark.baking.models.Ingredient;
import com.example.zark.baking.models.Recipe;
import com.example.zark.baking.utilities.MyNumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew Osborne on 7/11/17.
 *
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Ingredient> mIngredientList;

    public IngredientsAdapter(Context context, List<Ingredient> ingredientList) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mIngredientList = ingredientList;
    }

    @Override
    public IngredientsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = mLayoutInflater.inflate(R.layout.ingredient_item, parent, false);
        final IngredientsAdapterViewHolder viewHolder = new IngredientsAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientsAdapterViewHolder holder, int position) {

        // Get the current list of ingredients
        Ingredient currentIngredient = mIngredientList.get(position);
        holder.mQuantityTextView.setText(MyNumberUtils.formatQuantityToString(
                currentIngredient.getQuantity()));
        holder.mUnitTextView.setText(currentIngredient.getMeasure());
        holder.mMaterialTextView.setText(currentIngredient.getIngredient());


    }

    @Override
    public int getItemCount() {
        if (mIngredientList != null) {
            return mIngredientList.size();
        } else {
            return 0;
        }
    }

    public void setNewData(Recipe recipe) {
        if (recipe != null) {
            mIngredientList = recipe.getIngredients();
        }
        notifyDataSetChanged();
    }

    /**
     * Inner class to store and recycle views as they are scrolled off screen
     */
    public class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView mQuantityTextView;
        public TextView mUnitTextView;
        public TextView mMaterialTextView;

        public IngredientsAdapterViewHolder(View view) {
            super(view);

            mQuantityTextView = (TextView) view.findViewById(R.id.tv_ingredient_quantity);
            mUnitTextView = (TextView) view.findViewById(R.id.tv_ingredient_unit);
            mMaterialTextView = (TextView) view.findViewById(R.id.tv_ingredient_material);
        }
    }
}
