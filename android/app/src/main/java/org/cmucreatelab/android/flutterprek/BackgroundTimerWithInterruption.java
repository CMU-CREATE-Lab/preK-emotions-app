package org.cmucreatelab.android.flutterprek;

import android.os.Handler;

/**
 *
 * A single-use timer that calls a {@link TimeExpireListener} after the defined number of milliseconds elapses.
 *
 */
// TODO @tasota not sure if this other implementation is necessary? (merged from samspeer branch)
public class BackgroundTimerWithInterruption {

    private final Handler handler = new Handler();
    private final Runnable runnable;
    private final long millisecondsToWait;
    private boolean isStarted = false;


    public BackgroundTimerWithInterruption(long millisecondsToWait, final TimeExpireListener listener) {
        this.millisecondsToWait = millisecondsToWait;

        this.runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    listener.timerExpired();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(runnable, BackgroundTimerWithInterruption.this.millisecondsToWait);
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
        void timerExpired() throws InterruptedException;
    }

}
