package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static;

import org.cmucreatelab.android.flutterprek.R;

public class EmptyStaticCopingSkillActivity extends StaticCopingSkillActivity {


    @Override
    public String getAudioFileForCopingSkillTitle() {
        return null;
    }


    @Override
    public int getResourceForBackground() {
        return R.drawable.background_flower_sky;
    }


    @Override
    public int getTextTitleResource() {
        return R.string.coping_skill_static_placeholder;
    }

}
