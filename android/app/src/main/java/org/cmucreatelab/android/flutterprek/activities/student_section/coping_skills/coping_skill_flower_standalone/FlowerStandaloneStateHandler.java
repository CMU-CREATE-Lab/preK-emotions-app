package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower_standalone;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;

public class FlowerStandaloneStateHandler implements FlowerStandaloneBreathTracker.Listener {

    enum State {
        BEGIN,
        BREATHING,
        FINISHED
    }

    private static final long STEP_1_WAIT_IN_MILLISECONDS = 5000;

    private final FlowerStandaloneCopingSkillActivity activity;
    private final FlowerStandaloneBreathTracker breathTracker;
    private boolean isPressingButton = false;

    private BackgroundTimer timerForInstructions = new BackgroundTimer(STEP_1_WAIT_IN_MILLISECONDS, new BackgroundTimer.TimeExpireListener() {
        @Override
        public void timerExpired() {
            changeState(State.BREATHING);
        }
    });


    private void changeState(State newState) {
        if (newState == State.BEGIN) {
            activity.displayHoldFlowerInstructions();
            breathTracker.resetTracker();
        } else if (newState == State.BREATHING) {
            activity.displayBreatheIn();
            breathTracker.startTracker();
        } else if (newState == State.FINISHED) {
            breathTracker.resetTracker();
            activity.displayOverlay();
        }
    }


    public FlowerStandaloneStateHandler(FlowerStandaloneCopingSkillActivity activity) {
        this.activity = activity;
        this.breathTracker = new FlowerStandaloneBreathTracker(activity, this);
    }


    @Override
    public void onFinishedBreathing() {
        changeState(State.FINISHED);
    }


    public void initializeState() {
        timerForInstructions.stopTimer();
        changeState(State.BEGIN);
        timerForInstructions.startTimer();
    }


    public void pauseState() {
        // NOTE: we do not call changeState() method since it plays the audio prompt while in the background.
        //changeState(State.WAIT_FOR_BUTTON);
        timerForInstructions.stopTimer();
        breathTracker.resetTracker();
        // clear this flag (in case button was held down before entering this state)
        isPressingButton = false;
    }

}
