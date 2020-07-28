package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower_standalone;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower.FlowerCopingSkillProcess;

public class FlowerStandaloneCopingSkillStep1Timer implements BackgroundTimer.TimeExpireListener {

    private static final long TIME_TO_WAIT_IN_MILLISECONDS = 1200;

    private final BackgroundTimer timer;
    private FlowerStandaloneCopingSkillProcess flowerCopingSkillProcess;
    private boolean stateSwitchToA = false;


    public FlowerStandaloneCopingSkillStep1Timer(FlowerStandaloneCopingSkillProcess flowerCopingSkillProcess) {
        this.flowerCopingSkillProcess = flowerCopingSkillProcess;
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
        if (stateSwitchToA) {
            flowerCopingSkillProcess.goToStep(FlowerStandaloneCopingSkillProcess.StepNumber.STEP_1A_HOLD_FLOWER_LADYBUG);
        } else {
            flowerCopingSkillProcess.goToStep(FlowerStandaloneCopingSkillProcess.StepNumber.STEP_1B_HOLD_FLOWER_HAND);
        }
        stateSwitchToA = !stateSwitchToA;
    }

}
