package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import org.cmucreatelab.android.flutterprek.activities.fragments.AbstractFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video.VideoCopingSkillActivity;

public abstract class VideoFragment extends AbstractFragment {

    private ActivityCallback activityCallback;
    private @NonNull View fragmentView;

    public enum FragmentState {
        PLAY, MOVE_ON
    }

    public interface ActivityCallback extends MediaPlayer.OnCompletionListener {

        void setFragment(FragmentState fragmentState);

        void goToNextActivity();

        //void releaseOverlayTimers();

        //void restartOverlayTimers();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.fragmentView = view;
        fragmentView.setVisibility(View.GONE);
    }


    public void displayFragment(boolean display, ActivityCallback activity) {
        this.activityCallback = activity;
        fragmentView.setVisibility(display ? View.VISIBLE : View.GONE);
        if (display) {
            initializeFragment();
        }
    }


    public ActivityCallback getActivityCallback() {
        return activityCallback;
    }


    @NonNull
    public View getFragmentView() {
        return fragmentView;
    }


    /** this method runs to initialize the displayed fragment (example: visibility on views, class logic). */
    public abstract void initializeFragment();

}
