package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static;

import android.support.annotation.ColorRes;

import org.cmucreatelab.android.flutterprek.R;

public class YogaCopingSkillActivity extends VideoCopingSkillActivity {


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_jumping_jacks_coping_skill;
    }


    /** Get the color resource to use for the title (white by default). */
    @ColorRes
    public int getColorResourceForTitle() {
        return R.color.colorWhite;
    }


    @Override
    public String getVideoFileForCopingSkillTitle() {
        return "android.resource://" + getPackageName() + "/" + R.raw.yoga_sad;
    }


    public String getAudioFileForCopingSkillTitle() {
        return "etc/audio_prompts/audio_jumping_jacks.wav";
    }


    @Override
    public int getResourceForBackground() {
        return R.drawable.background_jumping_jacks;
    }


    @Override
    public int getTextTitleResource() {
        return R.string.empty;
    }


    @Override
    public boolean useAudioFromVideo() {
        return true;
    }

}
