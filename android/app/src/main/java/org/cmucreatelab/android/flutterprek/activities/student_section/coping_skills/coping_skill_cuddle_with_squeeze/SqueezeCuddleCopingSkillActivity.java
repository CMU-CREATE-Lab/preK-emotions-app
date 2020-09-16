package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_cuddle_with_squeeze;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.StaticCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.StaticCopingSkillTimeoutOverlay;

// TODO @tasota implement non-static coping skill
public class SqueezeCuddleCopingSkillActivity extends StaticCopingSkillActivity {

    private static final long DEFAULT_DISPLAY_OVERLAY_AFTER_MILLISECONDS = 32000;

    private final StaticCopingSkillTimeoutOverlay.OverlayOptionListener listener = new StaticCopingSkillTimeoutOverlay.OverlayOptionListener() {
        @Override
        public void onClickNo() {
            // does nothing
        }

        @Override
        public void onClickYes() {
            // does nothing
        }
    };


    @Override
    public long getMillisecondsToDisplayOverlay() {
        return DEFAULT_DISPLAY_OVERLAY_AFTER_MILLISECONDS;
    }


    @Override
    public StaticCopingSkillTimeoutOverlay createTimeoutOverlay() {
        return new StaticCopingSkillTimeoutOverlay(this, listener);
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        AudioPlayer.getInstance(getApplicationContext()).addAudioFromAssets(getAudioFileForDanceMusic());
//    }


    @Override
    public String getAudioFileForCopingSkillTitle() {
        return "etc/audio_prompts/audio_dance.wav";
    }


    @Override
    public int getResourceForBackground() {
        return R.drawable.sheep_background;
    }


    @Override
    public int getTextTitleResource() {
        // TODO replace
        return R.string.coping_skill_cuddle_no_manipulative;
    }

}
