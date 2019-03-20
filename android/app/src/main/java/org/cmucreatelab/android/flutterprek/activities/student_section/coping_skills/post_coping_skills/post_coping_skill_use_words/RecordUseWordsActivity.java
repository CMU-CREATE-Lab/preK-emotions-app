package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words;

import android.content.Intent;
import android.os.Bundle;

import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.fragments.RecordFragment;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;

public class RecordUseWordsActivity extends PostCopingSkillActivity {

    private RecordFragment recordFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // assign fragment
        this.recordFragment = (RecordFragment) (getSupportFragmentManager().findFragmentById(R.id.recordFragment));
        recordFragment.displayFragment(this);
    }


    @Override
    protected void onPause() {
        // Avoid recording while in background
        recordFragment.onPauseActivity();
        super.onPause();
        finish();
    }


    @Override
    public String getAudioFileForPostCopingSkillTitle() {
        return "etc/audio_prompts/audio_record_press_button.wav";
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_record;
    }


    public void goToNextPostCopingSkillActivity() {
        Intent intent = new Intent(this, MoveOnUseWordsActivity.class);
        startActivity(intent);
    }

}
