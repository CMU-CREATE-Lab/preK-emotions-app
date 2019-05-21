package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating.fragments.AnimateFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating.fragments.FeelHeartFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating.fragments.HeartRateFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating.fragments.VideoFragment;
import org.cmucreatelab.android.flutterprek.video.VideoPlayer;

import static org.cmucreatelab.android.flutterprek.SessionTracker.ITINERARY_INDEX;

public class HeartBeatingActivity extends PostCopingSkillActivity implements AnimateFragment.ActivityCallback {

    private int itineraryIndex;
    private FeelHeartFragment feelFragment;
    private HeartRateFragment rateFragment;


    @Override
    public String getAudioFileForPostCopingSkillTitle() {
        return "etc/audio_prompts/audio_heart_beating_how_fast.wav";
    }

    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_heart_beating;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itineraryIndex = getIntent().getIntExtra(ITINERARY_INDEX, -1);
        if (itineraryIndex < 0) {
            Log.e(Constants.LOG_TAG, "received bad (or default) value for ITINERARY_INDEX; ending session");
            GlobalHandler.getInstance(getApplicationContext()).endCurrentSession(this);
        }

        this.feelFragment = (FeelHeartFragment) (getSupportFragmentManager().findFragmentById(R.id.feelFragment));
        this.rateFragment = (HeartRateFragment) (getSupportFragmentManager().findFragmentById(R.id.rateFragment));

    }

    @Override
    protected void onResume() {
        setFragment(VideoFragment.FragmentState.PLAY);
        super.onResume();
    }

    @Override
    public void setFragment(VideoFragment.FragmentState fragmentState) {
        String videoToPlay;

        if (fragmentState == VideoFragment.FragmentState.PLAY) {
            feelFragment.displayFragment(true, this);
            rateFragment.displayFragment(false, this);
            videoToPlay  = "android.resource://" + getPackageName() + "/" + R.raw.hand_on_heart;
        } else {
            feelFragment.displayFragment(false, this);
            rateFragment.displayFragment(true, this);        }
        restartOverlayTimers();
        //VideoPlayer.getInstance(getApplicationContext()).playVideo(videoToPlay, getListener());

    }

    @Override
    public void goToNextActivity() {
        Intent intent = GlobalHandler.getInstance(getApplicationContext()).getSessionTracker().getNextIntentFromItinerary(this, itineraryIndex);
        startActivity(intent);
        // make sure you don't go to record, but rather the screen to choose to record.
        finish();
    }

}
