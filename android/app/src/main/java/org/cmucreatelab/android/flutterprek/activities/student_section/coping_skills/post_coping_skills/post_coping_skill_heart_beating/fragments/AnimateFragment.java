package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import org.cmucreatelab.android.flutterprek.activities.fragments.AbstractFragment;

public abstract class AnimateFragment extends AbstractFragment {

    private AnimateFragment.ActivityCallback activityCallback;
    private @NonNull
    View fragmentView;
    private AnimateFragment.FragmentState currentFragmentState;

    private int itineraryIndex;

    public enum FragmentState {
        PLAY,
        MOVE_ON
    }

    public interface ActivityCallback extends MediaPlayer.OnCompletionListener {

        void setFragment(AnimateFragment.FragmentState fragmentState);

        void releaseOverlayTimers();

        void restartOverlayTimers();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.fragmentView = view;
        fragmentView.setVisibility(View.GONE);
    }


    public AnimateFragment.ActivityCallback getActivityCallback() {
        return activityCallback;
    }


    public AnimateFragment.FragmentState getCurrentFragmentState() {
        return currentFragmentState;
    }


    @NonNull
    public View getFragmentView() {
        return fragmentView;
    }


    /**
     * Control display of fragments in an activity, given a current state.
     *
     * @param currentFragmentState The current fragment state which you are trying to display.
     * @param activity             An activity that implements the ActivityCallback interface.
     */
    public void displayFragment(AnimateFragment.FragmentState currentFragmentState, AnimateFragment.ActivityCallback activity) {
        this.currentFragmentState = currentFragmentState;
        this.activityCallback = activity;
    }


    /**
     * this method runs to initialize the displayed fragment (example: visibility on views, class logic).
     */
    public abstract void initializeFragment();

}