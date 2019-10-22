package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video;

import org.cmucreatelab.android.flutterprek.R;

public class ParentVideoCopingSkillActivity extends VideoCopingSkillActivity {


    @Override
    public int getResourceForBackground() {
        return R.color.colorDrawerBackground;
    }
    

    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_video;
    }


    @Override
    public boolean useAudioFromVideo() {
        return true;
    }


    @Override
    public String getFilePathForVideo() {
        return "android.resource://" + getPackageName() + "/" + R.raw.jumpingjacks;
    }

}
