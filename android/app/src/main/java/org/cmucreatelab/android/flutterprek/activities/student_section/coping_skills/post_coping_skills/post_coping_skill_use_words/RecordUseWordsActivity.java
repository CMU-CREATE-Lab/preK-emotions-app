package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.fragments.MoveOnFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.fragments.RecordFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.fragments.UseWordsFragment;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;

import static org.cmucreatelab.android.flutterprek.SessionTracker.ITINERARY_INDEX;

public class RecordUseWordsActivity extends PostCopingSkillActivity implements UseWordsFragment.ActivityCallback {

    private int itineraryIndex;
    private RecordFragment recordFragment;
    private MoveOnFragment moveOnFragment;


    @Override
    public String getAudioFileForPostCopingSkillTitle() {
        return null;
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_record_and_move_on;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itineraryIndex = getIntent().getIntExtra(ITINERARY_INDEX, -1);
        if (itineraryIndex < 0) {
            Log.e(Constants.LOG_TAG, "received bad (or default) value for ITINERARY_INDEX; ending session");
            GlobalHandler.getInstance(getApplicationContext()).endCurrentSession(this);
        }

        this.recordFragment = (RecordFragment) (getSupportFragmentManager().findFragmentById(R.id.recordFragment));
        this.moveOnFragment = (MoveOnFragment) (getSupportFragmentManager().findFragmentById(R.id.moveOnFragment));
    }


    @Override
    protected void onResume() {
        setFragment(UseWordsFragment.FragmentState.RECORD);
        super.onResume();
    }


    @Override
    protected void onPause() {
        // Avoid recording while in background
        recordFragment.onPauseActivity();
        super.onPause();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        // restart the timers after audio playback completes
        restartOverlayTimers();
    }


    public void setFragment(UseWordsFragment.FragmentState fragmentState) {
        String audioToPlay;
        if (fragmentState == UseWordsFragment.FragmentState.RECORD) {
            recordFragment.displayFragment(true, this);
            moveOnFragment.displayFragment(false, this);
            audioToPlay = "etc/audio_prompts/audio_record_press_button.wav";
        } else {
            recordFragment.displayFragment(false, this);
            moveOnFragment.displayFragment(true, this);
            audioToPlay = "etc/audio_prompts/audio_move_on.wav";
            moveOnFragment.addRecordedAudio(false);
        }
        AudioPlayer.getInstance(getApplicationContext()).stop();
        playAudio(audioToPlay);
        restartOverlayTimers();
    }


    public void goToNextActivity() {
        Intent intent = GlobalHandler.getInstance(getApplicationContext()).getSessionTracker().getNextIntentFromItinerary(this, itineraryIndex);
        startActivity(intent);
        // make sure you don't go to record, but rather the screen to choose to record.
        finish();
    }

}
