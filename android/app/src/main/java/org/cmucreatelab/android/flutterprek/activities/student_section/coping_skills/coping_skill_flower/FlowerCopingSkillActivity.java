package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;

public class FlowerCopingSkillActivity extends AbstractCopingSkillActivity {

    private FlowerCopingSkillProcess flowerCopingSkillProcess;
    private FlowerCopingSkillStep1Timer step1Timer;
    private FlowerStateHandler flowerStateHandler;


    private void playAudioInstructions() {
        playAudio("etc/audio_prompts/audio_flower_hold_thumb_ladybug.wav");
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

        flowerCopingSkillProcess = new FlowerCopingSkillProcess(this);
        step1Timer = new FlowerCopingSkillStep1Timer(flowerCopingSkillProcess);
        flowerStateHandler = new FlowerStateHandler(this);

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

        flowerStateHandler.initializeState();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Constants.LOG_TAG,"Stopping LeScan...");
        // avoid playing through after early exit
        flowerStateHandler.pauseState();
    }


    @Override
    protected void onResume() {
        super.onResume();
        flowerStateHandler.lookForFlower();
    }


    public void displayHoldFlowerInstructions() {
        flowerCopingSkillProcess.goToStep(FlowerCopingSkillProcess.StepNumber.STEP_1A_HOLD_FLOWER_LADYBUG);
        step1Timer.startTimer();
        playAudioInstructions();
    }


    public void displayBreatheIn() {
        step1Timer.stopTimer();
        flowerCopingSkillProcess.goToStep(FlowerCopingSkillProcess.StepNumber.STEP_2_SMELL);
        playAudioSmell();
    }


    public void displayBreatheOut() {
        step1Timer.stopTimer();
        flowerCopingSkillProcess.goToStep(FlowerCopingSkillProcess.StepNumber.STEP_3_BLOW);
        playAudioBlow();
    }


    public void displayOverlay() {
        flowerCopingSkillProcess.goToStep(FlowerCopingSkillProcess.StepNumber.STEP_4_OVERLAY);
        playAudioOverlay();
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_flower_coping_skill;
    }

}
