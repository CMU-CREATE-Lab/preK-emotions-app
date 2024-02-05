package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_flower_rainbow;

import android.animation.Animator;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;

public class FlowerRainbowCopingSkillActivity extends AbstractCopingSkillActivity {

    private FlowerRainbowCopingSkillProcess flowerCopingSkillProcess;
    private FlowerRainbowCopingSkillStep1Timer step1Timer;
    private FlowerRainbowStateHandler flowerStateHandler;
    private RainbowCreator rainbowCreator;
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

        flowerCopingSkillProcess = new FlowerRainbowCopingSkillProcess(this);
        step1Timer = new FlowerRainbowCopingSkillStep1Timer(flowerCopingSkillProcess);
        rainbowCreator = new RainbowCreator(this);
        flowerStateHandler = new FlowerRainbowStateHandler(this, rainbowCreator);

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


        // TODO calculate geometry for margins to form semi-circle (also inside a container?)
        // -- 12 stars, 6 colors with 2 stars each
        // -- margin left to right in fixed increments, sine function for y-values
        // -- Consider animation delay to "move" in an arc from left to right?
        //   +++ This might require lots of timers, async jobs, object instances mgmt, etc
        // TODO play with Interpolators?
        // -- https://developer.android.com/reference/android/view/animation/Interpolator
        //((ImageView)findViewById(R.id.imageViewAnim)).animate();
        Animation slow = AnimationUtils.loadAnimation(this, R.anim.heart_beat_slow);
        Animation fast = AnimationUtils.loadAnimation(this, R.anim.star_scale_fast);
        ImageView imageViewAnim = findViewById(R.id.imageViewAnim);
        ImageView imageViewAnim2 = findViewById(R.id.imageViewAnim2);
        imageViewAnim.startAnimation(slow);


//        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        rotate.setDuration(500);
//        rotate.setRepeatMode(Animation.REVERSE);
//        rotate.setRepeatCount(Animation.INFINITE);
//        rotate.setInterpolator(new LinearInterpolator());
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.star_rotate);
        rotate.setInterpolator(new LinearInterpolator());

        // https://stackoverflow.com/questions/6355495/animation-with-animationset-in-android
        // https://developer.android.com/reference/android/view/animation/AnimationSet.html
        AnimationSet animationSet = new AnimationSet(false);
        //animationSet.setInterpolator(new LinearInterpolator());
        animationSet.addAnimation(fast);
        animationSet.addAnimation(rotate);
        imageViewAnim2.startAnimation(animationSet);

        // TODO visibility and reset/restarting animations?
        // TODO use a fade in (one time) upon restarting animation (and start small to big)
        //imageViewAnim.setVisibility(View.INVISIBLE);

        rainbowCreator.animateRainbowFadeIn();

//        // TODO multiple animations?
//        imageViewAnim2.startAnimation(fast);
//        imageViewAnim2.startAnimation(rotate);
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_flower_rainbow_coping_skill;
    }


    public void displayHoldFlowerInstructions() {
        flowerCopingSkillProcess.goToStep(FlowerRainbowCopingSkillProcess.StepNumber.STEP_1A_HOLD_FLOWER_LADYBUG);
        step1Timer.startTimer();
        playAudioInstructions();
    }


    public void displayBreatheIn() {
        step1Timer.stopTimer();
        flowerCopingSkillProcess.goToStep(FlowerRainbowCopingSkillProcess.StepNumber.STEP_2_SMELL);
        playAudioSmell();
    }


    public void displayBreatheOut() {
        step1Timer.stopTimer();
        flowerCopingSkillProcess.goToStep(FlowerRainbowCopingSkillProcess.StepNumber.STEP_3_BLOW);
        playAudioBlow();
    }


    public void displayOverlay() {
        flowerCopingSkillProcess.goToStep(FlowerRainbowCopingSkillProcess.StepNumber.STEP_4_OVERLAY);
        playAudioOverlay();
    }


    public boolean isPaused() {
        return activityIsPaused;
    }

}
