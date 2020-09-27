package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_cuddle;

import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.StaticCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.StaticCopingSkillTimeoutOverlay;

public class CuddleCopingSkillTimeoutOverlay extends StaticCopingSkillTimeoutOverlay {

    private static final String AUDIO_FILE_PROMPT_MORE_TIME = "etc/audio_prompts/audio_sheep_standalone_overlay.wav";


    public CuddleCopingSkillTimeoutOverlay(StaticCopingSkillActivity activity) {
        super(activity);
    }


    public CuddleCopingSkillTimeoutOverlay(StaticCopingSkillActivity activity, final OverlayOptionListener listener) {
        super(activity, listener);
    }


    @Override
    public String getAudioFileForOverlayPrompt() {
        return AUDIO_FILE_PROMPT_MORE_TIME;
    }

}
