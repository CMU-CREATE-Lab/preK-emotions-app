package org.cmucreatelab.android.flutterprek.activities.student_section;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;

public class StudentSectionTimeoutOverlay {

    private static final long DISPLAY_OVERLAY_AFTER_MILLISECONDS = 180000; // 3 minutes
    private static final long DISMISS_OVERLAY_AFTER_MILLISECONDS = 15000;
    private static final String AUDIO_FILE_PROMPT_MORE_TIME = "etc/audio_prompts/audio_more_time.wav";

    private AbstractActivity activity;
    private final BackgroundTimer timerToDisplayOverlay, timerToExitFromOverlay;
    private boolean overlayIsDisplayed = false;


    private void finishSession() {
        releaseTimers();
        GlobalHandler.getInstance(activity.getApplicationContext()).endCurrentSession(activity);
    }


    private void displayOverlay() {
        timerToDisplayOverlay.stopTimer();
        overlayIsDisplayed = true;
        activity.playAudio(AUDIO_FILE_PROMPT_MORE_TIME);
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
        Log.i(Constants.LOG_TAG, "onTimerToExitFromOverlayExpired");
        finishSession();
    }


    public StudentSectionTimeoutOverlay(@NonNull AbstractActivity activity) {
        this.activity = activity;

        timerToDisplayOverlay = new BackgroundTimer(DISPLAY_OVERLAY_AFTER_MILLISECONDS, new BackgroundTimer.TimeExpireListener() {
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

        activity.findViewById(R.id.overlayYesNo).findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideOverlay();
            }
        });
        activity.findViewById(R.id.overlayYesNo).findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishSession();
            }
        });

        hideOverlay();
    }


    public void releaseTimers() {
        timerToDisplayOverlay.stopTimer();
        timerToExitFromOverlay.stopTimer();
    }


    public void restartTimers() {
        releaseTimers();
        if (overlayIsDisplayed) {
            timerToExitFromOverlay.startTimer();
        } else {
            timerToDisplayOverlay.startTimer();
        }
    }


    public void onPauseActivity() {
        releaseTimers();
    }


    public void onResumeActivity() {
        restartTimers();
    }

}
