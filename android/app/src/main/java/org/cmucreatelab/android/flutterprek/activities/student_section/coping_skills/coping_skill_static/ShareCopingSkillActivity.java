package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static;

import org.cmucreatelab.android.flutterprek.R;

public class ShareCopingSkillActivity extends StaticCopingSkillActivity {


    @Override
    public String getAudioFileForCopingSkillTitle() {
        return "etc/audio_prompts/audio_share.wav";
    }


    @Override
    public int getResourceForBackground() {
        return R.drawable.background_share;
    }


    @Override
    public int getTextTitleResource() {
        return R.string.coping_skill_share;
    }

}
