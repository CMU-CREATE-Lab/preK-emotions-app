package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills;

import android.media.MediaPlayer;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.activities.student_section.StudentSectionActivityWithTimeout;

public abstract class PostCopingSkillActivity extends StudentSectionActivityWithTimeout implements MediaPlayer.OnCompletionListener {


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(Constants.LOG_TAG, "PostCopingSkillActivity.onResume");
        // NOTE: might be null (e.g. heart beat activity)
        String filepathForPrompt = getAudioFileForPostCopingSkillTitle();
        if (filepathForPrompt != null) {
            startTimerToRepromptAndPlayAudio(filepathForPrompt);
        }
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        // NOTE: does nothing by default (override to implement)
    }


    /**
     * Every activity following the coping skill will have some prompt. This is the audio file associated with that prompt.
     *
     * @return relative path in assets for the audio file.
     */
    public abstract String getAudioFileForPostCopingSkillTitle();

}
