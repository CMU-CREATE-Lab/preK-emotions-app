package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_talk_to_the_teacher;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.StaticCopingSkillActivity;

public class TalkToTheTeacherCopingSkillActivity extends StaticCopingSkillActivity {

    private static final long DISPLAY_OVERLAY_AFTER_MILLISECONDS = 90000;


    @Override
    public String getAudioFileForCopingSkillTitle() {
        return "etc/audio_prompts/audio_talk_to_your_teacher.wav";
    }


    @Override
    public int getResourceForBackground() {
        return R.drawable.background_teacher;
    }


    @Override
    public int getTextTitleResource() {
        return R.string.coping_skill_teacher;
    }


    @Override
    public long getMillisecondsToDisplayOverlay() {
        return DISPLAY_OVERLAY_AFTER_MILLISECONDS;
    }

}
