package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating.fragments.HeartBeatingFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating.fragments.HowFastIsHeartBeatingFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating.fragments.PlaceHandOnHeartFragment;

import static org.cmucreatelab.android.flutterprek.SessionTracker.ITINERARY_INDEX;

public class HeartBeatingActivity extends PostCopingSkillActivity implements HeartBeatingFragment.ActivityCallback {

    private int itineraryIndex;
    private PlaceHandOnHeartFragment placeHandOnHeartFragment;
    private HowFastIsHeartBeatingFragment howFastIsHeartBeatingFragment;


    @Override
    public String getAudioFileForPostCopingSkillTitle() {
        return null;
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

        this.placeHandOnHeartFragment = (PlaceHandOnHeartFragment) (getSupportFragmentManager().findFragmentById(R.id.placeHandOnHeartFragment));
        this.howFastIsHeartBeatingFragment = (HowFastIsHeartBeatingFragment) (getSupportFragmentManager().findFragmentById(R.id.howFastIsHeartBeatingFragment));
    }


    @Override
    public void setFragment(HeartBeatingFragment.FragmentState fragmentState) {
        String audioToPlay;
        if (fragmentState == HeartBeatingFragment.FragmentState.PLACE_HAND_ON_HEART) {
            placeHandOnHeartFragment.displayFragment(true, this);
            howFastIsHeartBeatingFragment.displayFragment(false, this);
            audioToPlay = "etc/audio_prompts/audio_place_hand_on_heart.wav";
            playAudio(audioToPlay);
            restartOverlayTimers();
        } else {
            placeHandOnHeartFragment.displayFragment(false, this);
            howFastIsHeartBeatingFragment.displayFragment(true, this);

            audioToPlay = "etc/audio_prompts/audio_how_fast_is_heart_beating.wav";
            startTimerToRepromptAndPlayAudio(audioToPlay);
            restartOverlayTimers();
        }
    }


    @Override
    public void goToNextActivity() {
        Intent intent = GlobalHandler.getInstance(getApplicationContext()).getSessionTracker().getNextIntentFromItinerary(this, itineraryIndex);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onResume() {
        setFragment(HeartBeatingFragment.FragmentState.PLACE_HAND_ON_HEART);
        super.onResume();
    }

}
