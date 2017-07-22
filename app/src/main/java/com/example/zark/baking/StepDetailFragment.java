package com.example.zark.baking;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zark.baking.models.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment {

    private static final String TAG = StepDetailFragment.class.getSimpleName();
    private static final String KEY_SELECTED_STEP = "selectedStep";
    private static final int CONTROLS_VISIBLE = 1;
    private static final int CONTROLS_GONE = 0;
    private TextView mStepDescriptionTextView;
    private Step mCurrentStep;
    private Handler mMainHandler;
    private SimpleExoPlayer mPlayer;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private Uri mVideoUri;
    private int mVideoControlsVisible = CONTROLS_GONE;

    public StepDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_step_detail, container, false);

        mStepDescriptionTextView = (TextView) view.findViewById(R.id.tv_step_description);
        mSimpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.exo_player_view);

        // First time setup
        if (savedInstanceState == null) {
            if (getArguments() != null) {
                mCurrentStep = getArguments().getParcelable(KEY_SELECTED_STEP);
                if (generateVideoUriFromStep() != null) {
                    prepareVideoPlayer();
                    generateVideoUriFromStep();
                    showVideoPlayer();
                    playVideo();
                }
            }
        } else {
            mCurrentStep = savedInstanceState.getParcelable(KEY_SELECTED_STEP);
            if (generateVideoUriFromStep() != null) {
                prepareVideoPlayer();
                generateVideoUriFromStep();
                showVideoPlayer();
                playVideo();
            }
        }

        if (mCurrentStep != null) {
            displayStepDetails();
        } else {

        }

        return view;
    }

    private void showVideoPlayer() {

        mSimpleExoPlayerView.setVisibility(View.VISIBLE);


    }

    private void hideVideoPlayer() {
        mSimpleExoPlayerView.setVisibility(View.GONE);
    }

    private void prepareVideoPlayer() {
        // Create a default track selector for the video
        mMainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // Create the video player
        mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        // Bind the video player to the view
        mSimpleExoPlayerView.setPlayer(mPlayer);

    }

    public void playVideo() {

        if (mVideoUri == null || TextUtils.isEmpty(mVideoUri.toString())) {
            mSimpleExoPlayerView.setVisibility(View.GONE);
            return;
        }

        Log.v(TAG, "Attempting to play a video..." + mVideoUri);
        // Produces DataSource instances through which media data is loaded
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "baking"), null);
        // Produces Extractor instances for parsing the media data
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played
        MediaSource videoSource = new ExtractorMediaSource(mVideoUri,
                dataSourceFactory, extractorsFactory, null, null);
        // Prepare the player with the source
        mPlayer.prepare(videoSource);
        mPlayer.setPlayWhenReady(true);
    }

    public Uri generateVideoUriFromStep() {
        if (mCurrentStep == null || mCurrentStep.getVideoURL() == null ||
                TextUtils.isEmpty(mCurrentStep.getVideoURL())) {
            Log.v(TAG, "No video source found ");
            return null;
        }
        try {
            mVideoUri = Uri.parse(mCurrentStep.getVideoURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mVideoUri;
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

    @Override
    public void onStop() {
        super.onStop();
        if (mPlayer != null) {
            mPlayer.release();
        }
    }
}
