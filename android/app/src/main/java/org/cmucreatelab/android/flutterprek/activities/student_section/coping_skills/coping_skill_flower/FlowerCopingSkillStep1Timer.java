package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;

public class FlowerCopingSkillStep1Timer implements BackgroundTimer.TimeExpireListener {

    private final BackgroundTimer timer;
    private FlowerCopingSkillProcess flowerCopingSkillProcess;
    private boolean stateSwitchToA = false;
    private static final long MILLISECONDS_TO_WAIT = 1200;


    public FlowerCopingSkillStep1Timer(FlowerCopingSkillProcess flowerCopingSkillProcess) {
        this.flowerCopingSkillProcess = flowerCopingSkillProcess;
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
        if (stateSwitchToA) {
            flowerCopingSkillProcess.goToStep(FlowerCopingSkillProcess.StepNumber.STEP_1A_HOLD_FLOWER_LADYBUG);
        } else {
            flowerCopingSkillProcess.goToStep(FlowerCopingSkillProcess.StepNumber.STEP_1B_HOLD_FLOWER_HAND);
        }
        stateSwitchToA=!stateSwitchToA;
    }

}
