package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_squeeze;

import android.util.Log;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.ble.squeeze.BleSqueeze;

public class SqueezeWriteTimer implements BackgroundTimer.TimeExpireListener {

    private static final long TIME_TO_WAIT_IN_MILLISECONDS = 1000;
    // NOTE: firmware does not care what this message is as something is sent.
    private static final byte[] MESSAGE_TO_SEND = new byte[] { 0x01 };

    private final BackgroundTimer timer;
    private final SqueezeStateHandler squeezeStateHandler;


    public SqueezeWriteTimer(SqueezeStateHandler squeezeStateHandler) {
        this.squeezeStateHandler = squeezeStateHandler;
        this.timer = new BackgroundTimer(TIME_TO_WAIT_IN_MILLISECONDS, this);
    }


    public void startTimer() {
        timer.startTimer();
    }


    public void stopTimer() {
        timer.stopTimer();
    }


    @Override
    public void timerExpired() {
        BleSqueeze bleSqueeze = squeezeStateHandler.getBleSqueeze();
        if (bleSqueeze != null) {
            bleSqueeze.writeData(MESSAGE_TO_SEND);
        } else {
            Log.w(Constants.LOG_TAG, "SqueezeWriteTimer.timerExpired but BleSqueeze is null");
        }
        timer.startTimer();
    }

}
