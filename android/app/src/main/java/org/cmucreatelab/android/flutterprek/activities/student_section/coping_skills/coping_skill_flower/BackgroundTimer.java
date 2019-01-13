package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower;

import android.os.Handler;

/**
 *
 * A periodic timer that calls a {@link TimeExpireListener} after the defined number of milliseconds elapses.
 *
 */
public class BackgroundTimer {

    private final Handler handler = new Handler();
    private final Runnable runnable;
    private final long millisecondsToWait;
    private boolean isStarted = false;


    public BackgroundTimer(long millisecondsToWait, final TimeExpireListener listener) {
        this.millisecondsToWait = millisecondsToWait;

        this.runnable = new Runnable() {
            @Override
            public void run() {
                listener.timerExpired();
                handler.postDelayed(runnable, BackgroundTimer.this.millisecondsToWait);
            }
        };
    }


    public void startTimer() {
        if (!isStarted) {
            handler.postDelayed(runnable, millisecondsToWait);
            isStarted = true;
        }
    }


    public void stopTimer() {
        isStarted = false;
        handler.removeCallbacks(runnable);
    }


    public interface TimeExpireListener {
        void timerExpired();
    }

}
