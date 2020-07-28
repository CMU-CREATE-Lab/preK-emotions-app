package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower_standalone;

public class FlowerStandaloneStateHandler implements FlowerStandaloneBreathTracker.Listener {

    enum State {
        BREATHING,
        FINISHED
    }

    private final FlowerStandaloneCopingSkillActivity activity;
    private final FlowerStandaloneBreathTracker breathTracker;
    private boolean isPressingButton = false;


    private void changeState(State newState) {
        if (newState == State.BREATHING) {
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
        //changeState(State.WAIT_FOR_BUTTON);
        changeState(State.BREATHING);
    }


    public void pauseState() {
        // NOTE: we do not call changeState() method since it plays the audio prompt while in the background.
        //changeState(State.WAIT_FOR_BUTTON);
        breathTracker.resetTracker();
        // clear this flag (in case button was held down before entering this state)
        isPressingButton = false;
    }

}
