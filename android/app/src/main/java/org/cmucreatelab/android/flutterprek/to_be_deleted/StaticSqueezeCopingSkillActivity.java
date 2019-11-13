package org.cmucreatelab.android.flutterprek.to_be_deleted;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.StaticCopingSkillActivity;

public class StaticSqueezeCopingSkillActivity extends StaticCopingSkillActivity {


    @Override
    public String getAudioFileForCopingSkillTitle() {
        return "etc/audio_prompts/audio_squeeze.wav";
    }


    @Override
    public int getResourceForBackground() {
        return R.drawable.background_squeeze;
    }


    @Override
    public int getTextTitleResource() {
        return R.string.coping_skill_static_squeeze;
    }

}
