package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower_standalone;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower.FlowerBreathTracker;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower.FlowerWriteTimer;
import org.cmucreatelab.android.flutterprek.ble.bluetooth_birdbrain.UARTConnection;
import org.cmucreatelab.android.flutterprek.ble.flower.BleFlower;
import org.cmucreatelab.android.flutterprek.ble.flower.BleFlowerScanner;

public class FlowerStandaloneStateHandler implements FlowerStandaloneBreathTracker.Listener {

    enum State {
        WAIT_FOR_BUTTON,
        BREATHING,
        FINISHED
    }

    private final FlowerStandaloneCopingSkillActivity activity;
    private final FlowerStandaloneBreathTracker breathTracker;
//    private final BleFlowerScanner bleFlowerScanner;
    private final FlowerStandaloneWriteTimer flowerWriteTimer;
    private boolean isPressingButton = false;
    private BleFlower bleFlower;


    private void changeState(State newState) {
        if (newState == State.WAIT_FOR_BUTTON) {
            flowerWriteTimer.stopTimer();
            activity.displayHoldFlowerInstructions();
            breathTracker.resetTracker();
            // clear this flag (in case button was held down before entering this state)
            isPressingButton = false;
        } else if (newState == State.BREATHING) {
            flowerWriteTimer.startTimer();
            activity.displayBreatheIn();
            breathTracker.startTracker();
        } else if (newState == State.FINISHED) {
            flowerWriteTimer.stopTimer();
            breathTracker.resetTracker();
            activity.displayOverlay();
        }
    }


    public FlowerStandaloneStateHandler(FlowerStandaloneCopingSkillActivity activity) {
        this.activity = activity;
        this.breathTracker = new FlowerStandaloneBreathTracker(activity, this);
        this.flowerWriteTimer = new FlowerStandaloneWriteTimer(this);
    }


    @Override
    public void onFinishedBreathing() {
        changeState(State.FINISHED);
    }


    public void initializeState() {
        //changeState(State.WAIT_FOR_BUTTON);
        changeState(State.BREATHING);
    }


    public void pauseState() {
        // NOTE: we do not call changeState() method since it plays the audio prompt while in the background.
        //changeState(State.WAIT_FOR_BUTTON);
        flowerWriteTimer.stopTimer();
        breathTracker.resetTracker();
        // clear this flag (in case button was held down before entering this state)
        isPressingButton = false;
    }


    public BleFlower getBleFlower() {
        return bleFlower;
    }

}
