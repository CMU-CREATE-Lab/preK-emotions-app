package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_cuddle_with_squeeze;

import android.os.Bundle;
import android.view.View;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.StaticCopingSkillTimeoutOverlay;

public class SqueezeCuddleCopingSkillActivity extends AbstractCopingSkillActivity {

    private static final long DEFAULT_DISPLAY_OVERLAY_AFTER_MILLISECONDS = 32000;

    private boolean activityIsPaused = false;
    private SqueezeCuddleStateHandler squeezeCuddleStateHandler;
    private SqueezeCuddleCopingSkillAnimation squeezeCuddleCopingSkillAnimation;

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


    public void doSqueeze() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                squeezeCuddleCopingSkillAnimation.animateHeart();
            }
        });
    }


    public void displayOverlay(final boolean toDisplay) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (toDisplay) {
                    findViewById(R.id.overlayYesNo).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.overlayYesNo).setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        squeezeCuddleStateHandler = new SqueezeCuddleStateHandler(this);
        squeezeCuddleCopingSkillAnimation = new SqueezeCuddleCopingSkillAnimation(this);

        findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO initialize state again
            }
        });
        findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        displayOverlay(false);
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

}
