package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video;

import org.cmucreatelab.android.flutterprek.R;

public abstract class YogaCopingSkillActivity extends VideoCopingSkillActivity {


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_video;
    }


    @Override
    public int getResourceForBackground() {
        return R.drawable.background_stretching;
    }


    @Override
    public boolean useAudioFromVideo() {
        return true;
    }

}
