package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_finished_exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.UseWordsActivity;

public class FinishedExerciseActivity extends PostCopingSkillActivity {


    private void goToNextPostCopingSkillActivity() {
        Intent intent = new Intent(this, UseWordsActivity.class);
        startActivity(intent);
    }


    @Override
    public String getAudioFileForPostCopingSkillTitle() {
        return "etc/audio_prompts/audio_finished_exercise.wav";
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_finished_exercise;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.viewFeelingUnchanged).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO record choice (unchanged)
                goToNextPostCopingSkillActivity();
            }
        });
        findViewById(R.id.viewFeelingOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO record choice (ok)
                goToNextPostCopingSkillActivity();
            }
        });
        findViewById(R.id.viewFeelingHappy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO record choice (happy)
                goToNextPostCopingSkillActivity();
            }
        });
    }

}
