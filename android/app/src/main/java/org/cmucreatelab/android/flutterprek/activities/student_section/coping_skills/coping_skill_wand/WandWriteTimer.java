package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand;

import android.util.Log;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.ble.wand.BleWand;

public class WandWriteTimer implements BackgroundTimer.TimeExpireListener {

    private static final long TIME_TO_WAIT_IN_MILLISECONDS = 1000;
    // NOTE: firmware does not care what this message is as something is sent.
    private static final byte[] MESSAGE_TO_SEND = new byte[] { 0x01, 0x0D };

    private final BackgroundTimer timer;
    private final WandStateHandler wandStateHandler;


    public WandWriteTimer(WandStateHandler wandStateHandler) {
        this.wandStateHandler = wandStateHandler;
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
        BleWand bleWand = wandStateHandler.getBleWand();
        if (bleWand != null) {
            bleWand.writeData(MESSAGE_TO_SEND);
        } else {
            Log.w(Constants.LOG_TAG, "WandWriteTimer.timerExpired but BleSqueeze is null");
        }
        timer.startTimer();
    }

}
