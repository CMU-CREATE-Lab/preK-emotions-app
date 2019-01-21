package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills;

import android.media.MediaPlayer;
import android.os.Bundle;

import org.cmucreatelab.android.flutterprek.activities.student_section.StudentSectionActivityWithHeader;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.PostCopingSkillTimeoutOverlay;

public abstract class PostCopingSkillActivity extends StudentSectionActivityWithHeader implements MediaPlayer.OnCompletionListener {

    private PostCopingSkillTimeoutOverlay postCopingSkillTimeoutOverlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postCopingSkillTimeoutOverlay = new PostCopingSkillTimeoutOverlay(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        postCopingSkillTimeoutOverlay.onPauseActivity();
    }


    @Override
    protected void onResume() {
        super.onResume();
        playAudio(getAudioFileForPostCopingSkillTitle(), this);
        postCopingSkillTimeoutOverlay.onResumeActivity();
    }


    /**
     * Every activity following the coping skill will have some prompt. This is the audio file associated with that prompt.
     *
     * @return relative path in assets for the audio file.
     */
    public abstract String getAudioFileForPostCopingSkillTitle();


    /** NOTE: does nothing by default (override to implement) */
    @Override
    public void onCompletion(MediaPlayer mp) {

    }

}
