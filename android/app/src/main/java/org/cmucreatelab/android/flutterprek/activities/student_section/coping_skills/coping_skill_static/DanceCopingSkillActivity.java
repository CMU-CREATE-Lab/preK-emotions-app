package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;

public class DanceCopingSkillActivity extends StaticCopingSkillActivity {

    private static final long DEFAULT_DISPLAY_OVERLAY_AFTER_MILLISECONDS = 32000;

    private final StaticCopingSkillTimeoutOverlay.OverlayOptionListener listener = new StaticCopingSkillTimeoutOverlay.OverlayOptionListener() {
        @Override
        public void onClickNo() {
            // does nothing
        }

        @Override
        public void onClickYes() {
            AudioPlayer audioPlayer = AudioPlayer.getInstance(getApplicationContext());
            audioPlayer.stop();
            audioPlayer.addAudioFromAssets(getAudioFileForDanceMusic());
            audioPlayer.playAudio();
        }
    };


    private String getAudioFileForDanceMusic() {
        return "etc/music/MindfulDance2.mp3";
    }


    @Override
    public long getMillisecondsToDisplayOverlay() {
        return DEFAULT_DISPLAY_OVERLAY_AFTER_MILLISECONDS;
    }


    @Override
    public StaticCopingSkillTimeoutOverlay createTimeoutOverlay() {
        return new StaticCopingSkillTimeoutOverlay(this, listener);
    }


    @Override
    protected void onResume() {
        super.onResume();
        AudioPlayer.getInstance(getApplicationContext()).addAudioFromAssets(getAudioFileForDanceMusic());
    }


    @Override
    public String getAudioFileForCopingSkillTitle() {
        return "etc/audio_prompts/audio_dance.wav";
    }


    @Override
    public int getResourceForBackground() {
        return R.drawable.background_dance;
    }


    @Override
    public int getTextTitleResource() {
        return R.string.coping_skill_dance;
    }

}
