package com.example.zark.baking.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zark.baking.R;
import com.example.zark.baking.models.Recipe;
import com.example.zark.baking.models.Step;

import java.util.List;

/**
 * Created by Andrew Osborne on 7/11/17.
 *
 */

public class DirectionsAdapter
        extends RecyclerView.Adapter<DirectionsAdapter.DirectionsAdapterViewHolder> {

    private final MyDirectionsClickListener mClickHandler;
    private LayoutInflater mLayoutInflater;
    private List<Step> mStepsList;

    public interface MyDirectionsClickListener {
        void handleClick(int stepNumber);
    }

    public DirectionsAdapter(Context context, List<Step> stepsList, MyDirectionsClickListener listener) {
        mLayoutInflater = LayoutInflater.from(context);
        mStepsList = stepsList;
        mClickHandler = listener;
    }

    @Override
    public DirectionsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = mLayoutInflater.inflate(R.layout.direction_step_item, parent, false);
        final DirectionsAdapterViewHolder viewHolder = new DirectionsAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DirectionsAdapterViewHolder holder, final int position) {

        // Get the list of steps
        Step currentStep = mStepsList.get(position);
        holder.descriptionTextView.setText(currentStep.getShortDescription());
        int stepNumber = position + 1;
        holder.stepNumberTextView.setText(String.valueOf(stepNumber));

    }

    @Override
    public int getItemCount() {
        if (mStepsList != null) {
            return mStepsList.size();
        } else {
            return 0;
        }
    }

    public void setNewRecipe(Recipe recipe) {
        mStepsList = recipe.getSteps();
        notifyDataSetChanged();
    }

    /**
     * Inner class to store and recycle views as they are scrolled off screen
     */
    public class DirectionsAdapterViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        public TextView stepNumberTextView;
        public TextView descriptionTextView;
        public LinearLayout stepLayout;

        public DirectionsAdapterViewHolder(View view) {
            super(view);

            stepLayout = (LinearLayout) view.findViewById(R.id.direction_step_view);
            stepNumberTextView = (TextView) view.findViewById(R.id.tv_step_number);
            descriptionTextView = (TextView) view.findViewById(R.id.tv_step_description);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int stepNumber = getAdapterPosition()+1;
            if (mClickHandler != null) {
                mClickHandler.handleClick(stepNumber);
            }
        }
    }
}
