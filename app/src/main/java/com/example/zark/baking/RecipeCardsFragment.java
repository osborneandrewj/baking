package com.example.zark.baking;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zark.baking.adapters.RecipeCardAdapter;
import com.example.zark.baking.models.Recipe;
import com.example.zark.baking.retrofit.RecipeDbApi;
import com.example.zark.baking.retrofit.RecipeDbApiClient;
import com.example.zark.baking.testing.SimpleIdlingResource;
import com.example.zark.baking.utilities.MyNetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnRecipeSelectionListener} interface
 * to handle interaction events.
 */
public class RecipeCardsFragment extends Fragment implements
        RecipeCardAdapter.RecipeCardAdapterOnClickHandler {

    private static final String TAG = RecipeCardsFragment.class.getSimpleName();
    private static final int ONE_CARD_WIDE = 1;
    private static final int TWO_CARDS_WIDE = 2;
    private static final int THREE_CARDS_WIDE = 3;

    private List<Recipe> mRecipeList;

    private RecyclerView mRecyclerView;
    private RecipeCardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutmanager;
    private TextView mEmptyStateTextView;
    private RecipeDbApi mService;
    private boolean mTabletMode;
    private boolean mLandscapeMode = false;

    private OnRecipeSelectionListener mListener;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;
    // And not null for testing.
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    public RecipeCardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        getRecipeDataFromUdacity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //getRecipeDataFromUdacity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLandscapeMode = true;
        }
        if (getResources().getConfiguration().screenWidthDp >= 900) {
            mTabletMode = true;
        }

        View view = inflater.inflate(R.layout.fragment_recipe_cards, container, false);

        mRecyclerView = view.findViewById(R.id.recipe_cards_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        // Select gridlayout size based on screen width and orientation
        if (mTabletMode) {
            if (mLandscapeMode) {
                mLayoutmanager = new GridLayoutManager(getContext(), THREE_CARDS_WIDE);
            } else {
                mLayoutmanager = new GridLayoutManager(getContext(), TWO_CARDS_WIDE);
            }
        } else {
            if (mLandscapeMode) {
                mLayoutmanager = new GridLayoutManager(getContext(), TWO_CARDS_WIDE);
            } else {
                mLayoutmanager = new GridLayoutManager(getContext(), ONE_CARD_WIDE);
            }
        }
        mAdapter = new RecipeCardAdapter(getContext(), new ArrayList<Recipe>(), this);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutmanager);

        mEmptyStateTextView = view.findViewById(R.id.empty_view);
        hideEmptyState();

        return view;
    }

    /**
     * Use retrofit to retrieve recipe data from the Udacity server
     */
    public void getRecipeDataFromUdacity() {

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        if (mRecipeList != null) {
            mAdapter.setNewRecipeList(mRecipeList);
            return;
        }

        if (mService == null) {
            mService = RecipeDbApiClient.getClient().create(RecipeDbApi.class);
        }

        // Check for available network connection
        if (!MyNetworkUtils.doesNetworkConnectionExist(getContext())) {
            showEmptyState();
            return;
        }

        Call<List<Recipe>> callRecipes = mService.getRecipeList();
        callRecipes.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                hideEmptyState();
                mRecipeList = response.body();
                mAdapter.setNewRecipeList(mRecipeList);
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                t.printStackTrace();
                showEmptyState();
            }
        });
    }

    public void showEmptyState() {
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        }
        mRecyclerView.setVisibility(View.INVISIBLE);
        mEmptyStateTextView.setVisibility(View.VISIBLE);
    }

    public void hideEmptyState() {
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyStateTextView.setVisibility(View.GONE);
    }

    @Override
    public void onRecipeCardClick() {
        // Communicate with MainActivity
        if (mListener != null) {
            mListener.onRecipeSelection();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeSelectionListener) {
            mListener = (OnRecipeSelectionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeSelectionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRecipeSelectionListener {
        void onRecipeSelection();
    }

}
