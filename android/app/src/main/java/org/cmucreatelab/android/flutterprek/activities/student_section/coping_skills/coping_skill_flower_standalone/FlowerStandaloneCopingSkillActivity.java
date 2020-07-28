package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower_standalone;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower.FlowerCopingSkillStep1Timer;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower.FlowerStateHandler;

public class FlowerStandaloneCopingSkillActivity extends AbstractCopingSkillActivity {

    private FlowerStandaloneCopingSkillProcess flowerCopingSkillProcess;
    private FlowerStandaloneCopingSkillStep1Timer step1Timer;
    private FlowerStandaloneStateHandler flowerStateHandler;
    private boolean activityIsPaused;


    private void playAudioInstructions() {
        playAudio("etc/audio_prompts/audio_flower_button_stem.wav");
    }


    private void playAudioSmell() {
        playAudio("etc/audio_prompts/audio_flower_smell.wav");
    }


    private void playAudioBlow() {
        playAudio("etc/audio_prompts/audio_flower_blow.wav");
    }


    private void playAudioOverlay() {
        playAudio("etc/audio_prompts/audio_flower_again.wav");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flowerCopingSkillProcess = new FlowerStandaloneCopingSkillProcess(this);
        step1Timer = new FlowerStandaloneCopingSkillStep1Timer(flowerCopingSkillProcess);
        flowerStateHandler = new FlowerStandaloneStateHandler(this);

        // always hide debug/error windows
        findViewById(R.id.buttonConnectionError).setVisibility(View.GONE);
        findViewById(R.id.textViewDebug).setVisibility(View.GONE);

        findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flowerStateHandler.initializeState();
            }
        });
        findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onPause() {
        activityIsPaused = true;
        super.onPause();
        Log.d(Constants.LOG_TAG,"Stopping LeScan...");
        // avoid playing through after early exit
        flowerStateHandler.pauseState();
    }


    @Override
    protected void onResume() {
        activityIsPaused = false;
        super.onResume();
        flowerStateHandler.initializeState();
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_flower_coping_skill;
    }


    public void displayHoldFlowerInstructions() {
        flowerCopingSkillProcess.goToStep(FlowerStandaloneCopingSkillProcess.StepNumber.STEP_1A_HOLD_FLOWER_LADYBUG);
        step1Timer.startTimer();
        playAudioInstructions();
    }


    public void displayBreatheIn() {
        step1Timer.stopTimer();
        flowerCopingSkillProcess.goToStep(FlowerStandaloneCopingSkillProcess.StepNumber.STEP_2_SMELL);
        playAudioSmell();
    }


    public void displayBreatheOut() {
        step1Timer.stopTimer();
        flowerCopingSkillProcess.goToStep(FlowerStandaloneCopingSkillProcess.StepNumber.STEP_3_BLOW);
        playAudioBlow();
    }


    public void displayOverlay() {
        flowerCopingSkillProcess.goToStep(FlowerStandaloneCopingSkillProcess.StepNumber.STEP_4_OVERLAY);
        playAudioOverlay();
    }


    public boolean isPaused() {
        return activityIsPaused;
    }

}
