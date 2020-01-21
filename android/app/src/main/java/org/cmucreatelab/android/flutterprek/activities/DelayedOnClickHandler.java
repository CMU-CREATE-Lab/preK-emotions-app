package org.cmucreatelab.android.flutterprek.activities;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;

/**
 * Handles timer for setting the onclick listeners for an activity.
 */
public class DelayedOnClickHandler implements BackgroundTimer.TimeExpireListener {

    private static final long SET_ONCLICK_LISTENER_DELAY_IN_MILLISECONDS = 1000;

    private final BackgroundTimer backgroundTimer;
    private final AbstractActivity activity;
    private boolean delayIsFinished = false;


    public DelayedOnClickHandler(AbstractActivity activity) {
        this.activity = activity;
        backgroundTimer = new BackgroundTimer(SET_ONCLICK_LISTENER_DELAY_IN_MILLISECONDS, this);
    }


    public void onResumeActivity() {
        delayIsFinished = false;
        backgroundTimer.startTimer();
    }


    public void onPauseActivity() {
        backgroundTimer.stopTimer();
        delayIsFinished = false;
    }


    public boolean delayIsFinished() {
        return delayIsFinished;
    }


    @Override
    public void timerExpired() {
        delayIsFinished = true;
    }

}
