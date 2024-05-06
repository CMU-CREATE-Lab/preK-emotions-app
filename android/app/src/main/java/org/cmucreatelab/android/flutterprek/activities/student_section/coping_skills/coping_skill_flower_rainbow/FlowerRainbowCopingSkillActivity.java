package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower_rainbow;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.ble.flower.BleFlower;

public class FlowerRainbowCopingSkillActivity extends AbstractCopingSkillActivity implements FlowerRainbowStateHandler.BleBreathListener {

    private FlowerRainbowCopingSkillProcess flowerCopingSkillProcess;
    private FlowerRainbowCopingSkillStep1Timer step1Timer;
    private FlowerRainbowStateHandler flowerStateHandler;
    private boolean activityIsPaused;
    public int ledCountOnFlower = 0;
    private final Handler loopHandler = new Handler();
    private VectorAnimator vectorAnimator;
    private boolean readyToPlayStarAnimation = false;
    private boolean readyToPlayBottomStarAnimation = false;

    public int breathCount = 0;

    private static final int breathCountThresholdToDisplayBottomAnimation = 4;


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


    // TODO delete if unused (removed as of release-v2.3)
//    private void toggleBreathOnBleFlower(boolean enabled) {
//        BleFlower flower = flowerStateHandler.getBleFlower();
//        if (flower == null) {
//            Log.w(Constants.LOG_TAG, "tried to send ble data with no BleFlower; ignoring.");
//        } else {
//            byte[] msg = enabled ? "@b1".getBytes() : "@b0".getBytes();
//            flower.writeData(msg);
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flowerCopingSkillProcess = new FlowerRainbowCopingSkillProcess(this);
        step1Timer = new FlowerRainbowCopingSkillStep1Timer(flowerCopingSkillProcess);
        flowerStateHandler = new FlowerRainbowStateHandler(this, this);

        //this.vectorAnimator = new VectorAnimator(this, this);
        this.vectorAnimator = new VectorAnimator(this);
        vectorAnimator.addImageView(findViewById(R.id.imageViewStar1));
        vectorAnimator.addImageView(findViewById(R.id.imageViewStar2));
        vectorAnimator.addImageView(findViewById(R.id.imageViewStar3));
        vectorAnimator.addImageView(findViewById(R.id.imageViewStar4));
        vectorAnimator.addImageView(findViewById(R.id.imageViewStar5));
        vectorAnimator.addImageView(findViewById(R.id.imageViewStar6));
        vectorAnimator.addImageView(findViewById(R.id.imageViewStar7));
        vectorAnimator.addImageView(findViewById(R.id.imageViewStar8));
        vectorAnimator.addImageView(findViewById(R.id.imageViewStar9));
        vectorAnimator.addImageView(findViewById(R.id.imageViewStar10));
        vectorAnimator.addImageView(findViewById(R.id.imageViewStar11));
        vectorAnimator.addImageView(findViewById(R.id.imageViewStar12));
        vectorAnimator.addImageView(findViewById(R.id.imageViewBottomStar11));
        vectorAnimator.addImageView(findViewById(R.id.imageViewBottomStar10));
        vectorAnimator.addImageView(findViewById(R.id.imageViewBottomStar9));
        vectorAnimator.addImageView(findViewById(R.id.imageViewBottomStar8));
        vectorAnimator.addImageView(findViewById(R.id.imageViewBottomStar7));
        vectorAnimator.addImageView(findViewById(R.id.imageViewBottomStar6));
        vectorAnimator.addImageView(findViewById(R.id.imageViewBottomStar5));
        vectorAnimator.addImageView(findViewById(R.id.imageViewBottomStar4));
        vectorAnimator.addImageView(findViewById(R.id.imageViewBottomStar3));
        vectorAnimator.addImageView(findViewById(R.id.imageViewBottomStar2));

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
        flowerStateHandler.lookForFlower();
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_flower_rainbow_coping_skill;
    }


    public void displayHoldFlowerInstructions() {
        flowerCopingSkillProcess.goToStep(FlowerRainbowCopingSkillProcess.StepNumber.STEP_1A_HOLD_FLOWER_LADYBUG);
        step1Timer.startTimer();
        playAudioInstructions();
//        toggleBreathOnBleFlower(false);
    }


    public void displayBreatheIn() {
        this.readyToPlayStarAnimation = false;
        this.readyToPlayBottomStarAnimation = false;
        breathCount = 0;
        step1Timer.stopTimer();
        flowerCopingSkillProcess.goToStep(FlowerRainbowCopingSkillProcess.StepNumber.STEP_2_SMELL);
        playAudioSmell();
//        toggleBreathOnBleFlower(false);
    }


    public void displayBreatheOut() {
        this.readyToPlayStarAnimation = true;
        this.readyToPlayBottomStarAnimation = false;
        breathCount = 0;
        step1Timer.stopTimer();
        flowerCopingSkillProcess.goToStep(FlowerRainbowCopingSkillProcess.StepNumber.STEP_3_BLOW);
        playAudioBlow();
//        toggleBreathOnBleFlower(true);
    }


    public void displayOverlay() {
        flowerCopingSkillProcess.goToStep(FlowerRainbowCopingSkillProcess.StepNumber.STEP_4_OVERLAY);
        playAudioOverlay();
//        toggleBreathOnBleFlower(false);
    }


    public boolean isPaused() {
        return activityIsPaused;
    }


    @Override
    public void onReceivedData(boolean flowerDetectsBreathing) {
        if (flowerDetectsBreathing) {
            breathCount++;
            if (readyToPlayStarAnimation) {
                this.readyToPlayStarAnimation = false;
                loopHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vectorAnimator.startAnimations();
                    }
                }, 100);
            }
            if (readyToPlayBottomStarAnimation && breathCount >= breathCountThresholdToDisplayBottomAnimation) {
                this.readyToPlayBottomStarAnimation = false;
//                loopHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        bottomVectorAnimator.startAnimations();
//                    }
//                }, 100);
            }
        }
    }


//    @Override
//    public void OnAllAnimationsStarted(VectorAnimator vectorAnimator) {
//        // ASSERT: vectorAnimator == this.vectorAnimator
//        this.readyToPlayBottomStarAnimation = true;
//        // NOTE: check for count immediately for a smooth transition between animation sets
//        if (breathCount >= breathCountThresholdToDisplayBottomAnimation) {
//            this.readyToPlayBottomStarAnimation = false;
//            loopHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    bottomVectorAnimator.startAnimations();
//                }
//            }, 0);
//        }
//    }

}
