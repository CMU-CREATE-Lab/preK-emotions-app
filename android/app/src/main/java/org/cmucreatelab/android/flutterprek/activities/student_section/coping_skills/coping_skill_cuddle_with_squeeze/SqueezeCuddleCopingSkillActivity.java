package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_cuddle_with_squeeze;

import android.os.Bundle;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.StaticCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.StaticCopingSkillTimeoutOverlay;

// TODO @tasota implement non-static coping skill
public class SqueezeCuddleCopingSkillActivity extends AbstractCopingSkillActivity {

    private static final long DEFAULT_DISPLAY_OVERLAY_AFTER_MILLISECONDS = 32000;

    private boolean activityIsPaused = false;
    private SqueezeCuddleStateHandler squeezeCuddleStateHandler;

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


    public boolean isPaused() {
        return activityIsPaused;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        squeezeCuddleStateHandler = new SqueezeCuddleStateHandler(this);
    }

    @Override
    protected void onPause() {
        activityIsPaused = true;
        super.onPause();
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_squeeze_cuddle_coping_skill;
    }


    @Override
    protected void onResume() {
        activityIsPaused = false;
        squeezeCuddleStateHandler.lookForSqueeze();
        super.onResume();
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        AudioPlayer.getInstance(getApplicationContext()).addAudioFromAssets(getAudioFileForDanceMusic());
//    }

}
