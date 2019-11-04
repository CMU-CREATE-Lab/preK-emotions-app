package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower;

import android.util.Log;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.ble.flower.BleFlower;

public class FlowerWriteTimer implements BackgroundTimer.TimeExpireListener {

    private final BackgroundTimer timer;
    private final FlowerStateHandler flowerStateHandler;
    private boolean stateSwitchToA = false;

    private static final long MILLISECONDS_TO_WAIT = 1000;
    // NOTE: firmware does not care what the message is as long as you are sending something.
    private static final byte[] MESSAGE_TO_SEND = new byte[] { 0x01 };


    public FlowerWriteTimer(FlowerStateHandler flowerStateHandler) {
        this.flowerStateHandler = flowerStateHandler;
        this.timer = new BackgroundTimer(MILLISECONDS_TO_WAIT, this);
    }


    public void startTimer() {
        timer.startTimer();
    }


    public void stopTimer() {
        timer.stopTimer();
    }


    @Override
    public void timerExpired() {
        BleFlower bleFlower = flowerStateHandler.getBleFlower();
        if (bleFlower != null) {
            bleFlower.writeData(MESSAGE_TO_SEND);
        } else {
            Log.w(Constants.LOG_TAG, "FlowerWriteTimer.timerExpired but BleFlower is null");
        }
        timer.startTimer();
    }

}
