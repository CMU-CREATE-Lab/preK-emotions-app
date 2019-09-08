package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_squeeze;

import android.view.View;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.R;

public class SqueezeCopingSkillProcess {
    private static final long SQUEEZE_DURATION = 170000;
    private static final long DISMISS_OVERLAY_AFTER_MILLISECONDS = 10000;
    private BackgroundTimer timerToDisplayOverlay, timerToExitFromOverlay;
    private boolean overlayIsDisplayed = false;
    private final SqueezeCopingSkillActivity squeezeCopingSkillActivity;


    private void releaseTimers() {
        timerToDisplayOverlay.stopTimer();
        timerToExitFromOverlay.stopTimer();
    }


    private void finishActivity() {
        releaseTimers();
        squeezeCopingSkillActivity.finish();
    }


    private void displayOverlay() {
        timerToDisplayOverlay.stopTimer();
        overlayIsDisplayed = true;
        squeezeCopingSkillActivity.findViewById(R.id.overlayYesNo).setVisibility(View.VISIBLE);
        //stopSqueezing();
        timerToDisplayOverlay.stopTimer();
        timerToExitFromOverlay.startTimer();
    }


    private void hideOverlay() {
        releaseTimers();
        overlayIsDisplayed = false;
        squeezeCopingSkillActivity.findViewById(R.id.overlayYesNo).setVisibility(View.GONE);
        timerToDisplayOverlay.startTimer();
    }


    private void onTimerToDisplayOverlayExpired() {
        displayOverlay();
    }


    private void onTimerToExitFromOverlayExpired() {
        finishActivity();
    }


    public SqueezeCopingSkillProcess(final SqueezeCopingSkillActivity squeezeCopingSkillActivity) {
        this.squeezeCopingSkillActivity = squeezeCopingSkillActivity;

        timerToDisplayOverlay = new BackgroundTimer(SQUEEZE_DURATION, new BackgroundTimer.TimeExpireListener() {
            @Override
            public void timerExpired() {
                timerToDisplayOverlay.stopTimer();
                onTimerToDisplayOverlayExpired();
            }
        });
        timerToExitFromOverlay = new BackgroundTimer(DISMISS_OVERLAY_AFTER_MILLISECONDS, new BackgroundTimer.TimeExpireListener() {
            @Override
            public void timerExpired() {
                timerToExitFromOverlay.stopTimer();
                onTimerToExitFromOverlayExpired();
            }
        });

        squeezeCopingSkillActivity.findViewById(R.id.overlayYesNo).findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Fix this so it starts the activity over again
                squeezeCopingSkillActivity.setScreen();
                timerToDisplayOverlay.startTimer();
                hideOverlay();
                //startSqueezing();
            }
        });
        squeezeCopingSkillActivity.findViewById(R.id.overlayYesNo).findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        hideOverlay();
    }


    public void onPauseActivity() {

        releaseTimers();
    }


    public void onResumeActivity() {
        if (overlayIsDisplayed) {
            timerToExitFromOverlay.startTimer();
        } else {
            timerToDisplayOverlay.startTimer();
        }
    }


    public void startSqueezing(){
        // do something
    }


    public void stopSqueezing () {
        // do something
        timerToDisplayOverlay.stopTimer();
    }

}