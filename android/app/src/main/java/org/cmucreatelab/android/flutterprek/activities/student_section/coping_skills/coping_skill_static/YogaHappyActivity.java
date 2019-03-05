package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static;

import org.cmucreatelab.android.flutterprek.R;

public class YogaHappyActivity extends YogaCopingSkillActivity {


    @Override
    public String getVideoFileForCopingSkillTitle() {
        return "android.resource://" + getPackageName() + "/" + R.raw.yoga_happy;
    }

}
