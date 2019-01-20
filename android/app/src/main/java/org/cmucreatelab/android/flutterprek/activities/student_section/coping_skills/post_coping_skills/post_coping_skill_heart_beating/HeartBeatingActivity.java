package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_finished_exercise.FinishedExerciseActivity;

public class HeartBeatingActivity extends PostCopingSkillActivity {


    private void goToNextPostCopingSkillActivity() {
        Intent intent = new Intent(this, FinishedExerciseActivity.class);
        startActivity(intent);
    }


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

        Animation slow = AnimationUtils.loadAnimation(this, R.anim.heart_beat_slow);
        Animation fast = AnimationUtils.loadAnimation(this, R.anim.heart_beat_fast);
        ImageView heartSlow = findViewById(R.id.imageViewBeatingSlow);
        ImageView heartFast = findViewById(R.id.imageViewBeatingFast);

        heartSlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO record choice (slow)
                goToNextPostCopingSkillActivity();
            }
        });
        heartFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO record choice (fast)
                goToNextPostCopingSkillActivity();
            }
        });

        heartSlow.startAnimation(slow);
        heartFast.startAnimation(fast);
    }

}
