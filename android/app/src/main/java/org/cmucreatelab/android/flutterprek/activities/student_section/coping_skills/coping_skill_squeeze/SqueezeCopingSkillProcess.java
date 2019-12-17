package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_squeeze;

import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.R;

public class SqueezeCopingSkillProcess {

    private static final long SQUEEZE_IDLE_DURATION_MILLISECONDS = 25000;
    private static final long DISMISS_OVERLAY_AFTER_MILLISECONDS = 15000;

    // TODO make these non-static?
    private static BackgroundTimer timerToDisplayOverlay, timerToExitFromOverlay;

    private boolean overlayIsDisplayed = false;
    private final SqueezeCopingSkillActivity squeezeCopingSkillActivity;


    private void finishActivity() {
        releaseTimers();
        squeezeCopingSkillActivity.finish();
    }


    private void displayOverlay() {
        overlayIsDisplayed = true;
        squeezeCopingSkillActivity.findViewById(R.id.overlayYesNo).setVisibility(View.VISIBLE);
        if (SqueezeStateHandler.getCurrentState() != SqueezeStateHandler.State.ACTIVITY_END) {
            ((TextView) squeezeCopingSkillActivity.findViewById(R.id.textViewOverlayTitle)).setText(R.string.overlay_placeholder);
        }
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

        timerToDisplayOverlay = new BackgroundTimer(SQUEEZE_IDLE_DURATION_MILLISECONDS, new BackgroundTimer.TimeExpireListener() {
            @Override
            public void timerExpired() {
                onTimerToDisplayOverlayExpired();
            }
        });
        timerToExitFromOverlay = new BackgroundTimer(DISMISS_OVERLAY_AFTER_MILLISECONDS, new BackgroundTimer.TimeExpireListener() {
            @Override
            public void timerExpired() {
                onTimerToExitFromOverlayExpired();
            }
        });

        squeezeCopingSkillActivity.findViewById(R.id.overlayYesNo).findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SqueezeStateHandler.getCurrentState() == SqueezeStateHandler.State.ACTIVITY_END) {
                    squeezeCopingSkillActivity.recreate();
                } else {
                    hideOverlay();
                }
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


    public static void resetTimers() {
        releaseTimers();
        timerToDisplayOverlay.startTimer();
    }


    public void onPauseActivity() {
        releaseTimers();
    }


    public static void releaseTimers() {
        timerToDisplayOverlay.stopTimer();
        timerToExitFromOverlay.stopTimer();
    }


    public void onResumeActivity() {
        if (overlayIsDisplayed) {
            timerToExitFromOverlay.startTimer();
        } else {
            timerToDisplayOverlay.startTimer();
        }
    }

}