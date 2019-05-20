package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating.fragments;

import android.util.Log;
import android.view.View;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;

import java.io.File;

public class FeelHeartFragment extends VideoFragment {


    @Override
    public int getInflatedLayoutResource() {
        return R.layout._coping_skill__fragment_heart;
    }


    @Override
    public void initializeFragment() {
        final ActivityCallback activityCallback = getActivityCallback();

    }



    public void stopPlayingAndMoveOn() {
        getActivityCallback().setFragment(FragmentState.MOVE_ON);
    }


    public void onPauseActivity() {
    }

}
