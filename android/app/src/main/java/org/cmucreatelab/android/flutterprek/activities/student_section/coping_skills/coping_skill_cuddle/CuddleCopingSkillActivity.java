package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_cuddle;

import android.os.Bundle;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.StaticCopingSkillActivity;

public class CuddleCopingSkillActivity extends StaticCopingSkillActivity {

    private static final long DISPLAY_OVERLAY_AFTER_MILLISECONDS = 90000;
    private CuddleCopingSkillAnimation cuddleCopingSkillAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cuddleCopingSkillAnimation = new CuddleCopingSkillAnimation(this);
    }

    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_cuddle_coping_skill;
    }


    @Override
    public String getAudioFileForCopingSkillTitle() {
        return "etc/audio_prompts/audio_cuddle.wav";
    }


    @Override
    public int getResourceForBackground() {
        return R.drawable.background_cuddle_alternative;
    }


    @Override
    public int getTextTitleResource() {
        return R.string.coping_skill_cuddle;
    }


    @Override
    public long getMillisecondsToDisplayOverlay() {
        return DISPLAY_OVERLAY_AFTER_MILLISECONDS;
    }

}
