package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.fragments.MoveOnFragment;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;

public class MoveOnUseWordsActivity extends PostCopingSkillActivity {

    private MoveOnFragment moveOnFragment;


    @Override
    protected void onResume() {
        // add recorded message first (no playback)
        moveOnFragment.addRecordedAudio(false);
        // then add prompt (plays recording first, then prompt)
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // assign fragment
        this.moveOnFragment = (MoveOnFragment) (getSupportFragmentManager().findFragmentById(R.id.moveOnFragment));
        moveOnFragment.displayFragment(this);
    }


    @Override
    public String getAudioFileForPostCopingSkillTitle() {
        return "etc/audio_prompts/audio_move_on.wav";
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_move_on;
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        // restart the timers after audio playback completes
        restartOverlayTimers();
    }


    public void goToNextPostCopingSkillActivity(Class nextClass) {
        Intent intent = new Intent(this, nextClass);
        // avoid making new instance of RecordUseWordsActivity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
