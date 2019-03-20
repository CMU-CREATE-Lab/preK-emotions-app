package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_rejoin_friends.RejoinFriendsActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.fragments.MoveOnFragment;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.fragments.RecordFragment;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;

public class RecordUseWordsActivity extends PostCopingSkillActivity {

    private RecordFragment recordFragment;
    private MoveOnFragment moveOnFragment;

    public enum FragmentState {
        RECORD, MOVE_ON
    }


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
        this.recordFragment = (RecordFragment) (getSupportFragmentManager().findFragmentById(R.id.recordFragment));
        this.moveOnFragment = (MoveOnFragment) (getSupportFragmentManager().findFragmentById(R.id.moveOnFragment));
    }


    @Override
    protected void onResume() {
        setCurrentFragment(FragmentState.RECORD);
        super.onResume();
    }


    @Override
    protected void onPause() {
        // Avoid recording while in background
        recordFragment.onPauseActivity();
        super.onPause();
    }


    public void setCurrentFragment(FragmentState fragmentState) {
        String audioToPlay;
        if (fragmentState == FragmentState.RECORD) {
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


    public void goToNextPostCopingSkillActivity() {
        Intent intent = new Intent(this, RejoinFriendsActivity.class);
        // avoid making new instance of RecordUseWordsActivity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        // restart the timers after audio playback completes
        restartOverlayTimers();
    }

}
