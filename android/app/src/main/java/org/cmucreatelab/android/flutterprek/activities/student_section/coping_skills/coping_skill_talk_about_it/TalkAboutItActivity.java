package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_talk_about_it;

import android.media.MediaPlayer;
import android.os.Bundle;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.StudentSectionTimeoutOverlay;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.fragments.MoveOnFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.fragments.RecordFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.fragments.UseWordsFragment;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;

public class TalkAboutItActivity extends AbstractCopingSkillActivity implements UseWordsFragment.ActivityCallback {

    private StudentSectionTimeoutOverlay timeoutOverlay;
    private RecordFragment recordFragment;
    private MoveOnFragment moveOnFragment;


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_talk_about_it;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.timeoutOverlay = new StudentSectionTimeoutOverlay(this);
        this.recordFragment = (RecordFragment) (getSupportFragmentManager().findFragmentById(R.id.recordFragment));
        this.moveOnFragment = (MoveOnFragment) (getSupportFragmentManager().findFragmentById(R.id.moveOnFragment));
    }


    @Override
    protected void onResume() {
        setFragment(UseWordsFragment.FragmentState.RECORD);
        super.onResume();
        timeoutOverlay.onResumeActivity();
    }


    @Override
    protected void onPause() {
        // Avoid recording while in background
        recordFragment.onPauseActivity();
        super.onPause();
        timeoutOverlay.onPauseActivity();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        // restart the timers after audio playback completes
        restartOverlayTimers();
    }


    @Override
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


    @Override
    public void goToNextActivity() {
        finish();
    }


    @Override
    public void releaseOverlayTimers() {
        timeoutOverlay.releaseTimers();
    }


    @Override
    public void restartOverlayTimers() {
        timeoutOverlay.restartTimers();
    }

}
