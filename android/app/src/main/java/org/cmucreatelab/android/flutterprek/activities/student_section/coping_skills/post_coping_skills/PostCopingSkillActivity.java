package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills;

import org.cmucreatelab.android.flutterprek.activities.student_section.StudentSectionActivityWithHeader;

public abstract class PostCopingSkillActivity extends StudentSectionActivityWithHeader {


    @Override
    protected void onResume() {
        super.onResume();
        playAudio(getAudioFileForPostCopingSkillTitle());
    }


    /**
     * Every activity following the coping skill will have some prompt. This is the audio file associated with that prompt.
     *
     * @return relative path in assets for the audio file.
     */
    public abstract String getAudioFileForPostCopingSkillTitle();

}
