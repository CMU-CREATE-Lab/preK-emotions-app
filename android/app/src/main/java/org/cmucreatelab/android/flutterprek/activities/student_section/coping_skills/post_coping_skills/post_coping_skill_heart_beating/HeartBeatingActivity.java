package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_heart_beating;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;

public class HeartBeatingActivity extends PostCopingSkillActivity {


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

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.heart_beat_slow);
        findViewById(R.id.imageView1).startAnimation(anim);
        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.heart_beat_fast);
        findViewById(R.id.imageView2).startAnimation(anim2);
    }

}
