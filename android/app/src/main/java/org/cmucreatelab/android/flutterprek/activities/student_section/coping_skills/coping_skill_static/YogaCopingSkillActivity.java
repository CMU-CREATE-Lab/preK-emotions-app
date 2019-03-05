package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static;

import org.cmucreatelab.android.flutterprek.R;

public class YogaCopingSkillActivity extends VideoCopingSkillActivity {


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_video;
    }


    @Override
    public String getVideoFileForCopingSkillTitle() {
        return "android.resource://" + getPackageName() + "/" + R.raw.yoga_mad_excited;
    }


    @Override
    public int getResourceForBackground() {
        return R.drawable.background_flower_sky;
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
