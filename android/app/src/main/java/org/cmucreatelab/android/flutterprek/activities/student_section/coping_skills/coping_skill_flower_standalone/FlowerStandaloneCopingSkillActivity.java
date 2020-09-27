package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower_standalone;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;

public class FlowerStandaloneCopingSkillActivity extends AbstractCopingSkillActivity {

    private FlowerStandaloneCopingSkillProcess flowerCopingSkillProcess;
    private FlowerStandaloneStateHandler flowerStateHandler;
    private boolean activityIsPaused;


    private void playAudioInstructions() {
        playAudio("etc/audio_prompts/audio_flower_hold.wav");
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
        flowerStateHandler = new FlowerStandaloneStateHandler(this);

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
        return R.layout.activity_flower2;
    }


    public void displayHoldFlowerInstructions() {
        flowerCopingSkillProcess.goToStep(FlowerStandaloneCopingSkillProcess.StepNumber.STEP_1_HOLD);
        playAudioInstructions();
    }


    public void displayBreatheIn() {
        flowerCopingSkillProcess.goToStep(FlowerStandaloneCopingSkillProcess.StepNumber.STEP_2_SMELL);
        playAudioSmell();
    }


    public void displayBreatheOut() {
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
