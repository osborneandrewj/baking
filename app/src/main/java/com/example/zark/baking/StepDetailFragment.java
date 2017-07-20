package com.example.zark.baking;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zark.baking.models.Recipe;
import com.example.zark.baking.models.Step;



/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment {

    private static final String KEY_SELECTED_STEP = "selectedStep";
    private TextView mStepDescriptionTextView;
    private Step mCurrentStep;

    public StepDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_step_detail, container, false);

        mStepDescriptionTextView = view.findViewById(R.id.tv_step_description);

        if (savedInstanceState == null) {
            if (getArguments() != null) {
                mCurrentStep = getArguments().getParcelable(KEY_SELECTED_STEP);
                Log.v("detail", "step: " + mCurrentStep.getDescription());
            }
        } else {
            mCurrentStep = savedInstanceState.getParcelable(KEY_SELECTED_STEP);
        }

        if (mCurrentStep != null) {
            displayStepDetails();
        } else {

        }

        return view;
    }

    public void displayStepDetails() {
        if (mCurrentStep != null) {
            mStepDescriptionTextView.setText(mCurrentStep.getDescription());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_SELECTED_STEP, mCurrentStep);

    }

}
