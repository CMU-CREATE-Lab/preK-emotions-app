package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills;

import android.content.Intent;
import android.view.View;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.ChooseStudentActivity;

public class StaticCopingSkillTimeoutOverlay {

    private static final long DISPLAY_OVERLAY_AFTER_MILLISECONDS = 30000;
    private static final long DISMISS_OVERLAY_AFTER_MILLISECONDS = 10000;
    private AbstractActivity activity;
    private BackgroundTimer timerToDisplayOverlay, timerToExitFromOverlay;
    private boolean overlayIsDisplayed = false;


//    // NOTE: this is timing out for being idle
//    private void finishSession() {
//        Intent intent = new Intent(activity, ChooseStudentActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        activity.startActivity(intent);
//    }


    private void releaseTimers() {
        timerToDisplayOverlay.stopTimer();
        timerToExitFromOverlay.stopTimer();
    }


    private void finishActivity() {
        releaseTimers();
        activity.finish();
    }


    private void displayOverlay() {
        timerToDisplayOverlay.stopTimer();
        overlayIsDisplayed = true;
        activity.findViewById(R.id.overlayYesNo).setVisibility(View.VISIBLE);
        timerToExitFromOverlay.startTimer();
    }


    private void hideOverlay() {
        releaseTimers();
        overlayIsDisplayed = false;
        activity.findViewById(R.id.overlayYesNo).setVisibility(View.GONE);
        timerToDisplayOverlay.startTimer();
    }


    private void onTimerToDisplayOverlayExpired() {
        displayOverlay();
    }


    private void onTimerToExitFromOverlayExpired() {
        finishActivity();
    }


    public StaticCopingSkillTimeoutOverlay(AbstractActivity activity) {
        this.activity = activity;

        timerToDisplayOverlay = new BackgroundTimer(DISPLAY_OVERLAY_AFTER_MILLISECONDS, new BackgroundTimer.TimeExpireListener() {
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

        activity.findViewById(R.id.overlayYesNo).findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideOverlay();
            }
        });
        activity.findViewById(R.id.overlayYesNo).findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
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

}
