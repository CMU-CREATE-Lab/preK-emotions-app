package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static;

import org.cmucreatelab.android.flutterprek.R;

public class CuddleCopingSkillActivity extends StaticCopingSkillActivity {

    private static final long DISPLAY_OVERLAY_AFTER_MILLISECONDS = 90000;


    @Override
    public String getAudioFileForCopingSkillTitle() {
        return "etc/audio_prompts/audio_cuddle.wav";
    }


    @Override
    public int getResourceForBackground() {
        return R.drawable.background_cuddle;
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
