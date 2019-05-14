package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand;

import android.graphics.Point;
import android.support.constraint.Guideline;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;

public class WandCopingSkillProcess {

    private static final long SONG_DURATION = 23000;
    private static final long TEMPO = 1000;
    private static final long DISMISS_OVERLAY_AFTER_MILLISECONDS = 10000;
    private BackgroundTimer timerToDisplayOverlay, timerToExitFromOverlay;
    private boolean overlayIsDisplayed = false;
    private final WandCopingSkillActivity wandCopingSkillActivity;

    float handWidth;

    private void releaseTimers() {
        timerToDisplayOverlay.stopTimer();
        timerToExitFromOverlay.stopTimer();
    }

    private void finishActivity() {
        releaseTimers();
        wandCopingSkillActivity.finish();
    }

    private void displayOverlay() {
        timerToDisplayOverlay.stopTimer();
        overlayIsDisplayed = true;
        wandCopingSkillActivity.findViewById(R.id.overlayYesNo).setVisibility(View.VISIBLE);
        timerToExitFromOverlay.startTimer();
    }

    private void hideOverlay() {
        releaseTimers();
        overlayIsDisplayed = false;
        wandCopingSkillActivity.findViewById(R.id.overlayYesNo).setVisibility(View.GONE);
        timerToDisplayOverlay.startTimer();
    }

    private void onTimerToDisplayOverlayExpired() {
        displayOverlay();
    }

    private void onTimerToExitFromOverlayExpired() {
        finishActivity();
    }

    public WandCopingSkillProcess(WandCopingSkillActivity wandCopingSkillActivity) {
        this.wandCopingSkillActivity = wandCopingSkillActivity;

        timerToDisplayOverlay = new BackgroundTimer(SONG_DURATION, new BackgroundTimer.TimeExpireListener() {
            @Override
            public void timerExpired() {
                timerToDisplayOverlay.stopTimer();
                onTimerToDisplayOverlayExpired();
            }
        });
        timerToExitFromOverlay = new BackgroundTimer(DISMISS_OVERLAY_AFTER_MILLISECONDS, new BackgroundTimer.TimeExpireListener() {
            @Override
            public void timerExpired() {
                timerToExitFromOverlay.stopTimer();
                onTimerToExitFromOverlayExpired();
            }
        });

        wandCopingSkillActivity.findViewById(R.id.overlayYesNo).findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Fix this so it starts the activity over again
                hideOverlay();
                playSong();
                startWandMoving();
            }
        });
        wandCopingSkillActivity.findViewById(R.id.overlayYesNo).findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        hideOverlay();
    }

    public void onPauseActivity() {
        releaseTimers();
    }


    public void onResumeActivity() {
        if (overlayIsDisplayed) {
            timerToExitFromOverlay.startTimer();
        } else {
            timerToDisplayOverlay.startTimer();
        }
    }

    public void test(){
        final ImageView wandHand = wandCopingSkillActivity.findViewById(R.id.imageViewWandHand);
        handWidth = wandHand.getWidth();
    }

    public void startWandMoving(){
        // TODO clean up
        Guideline rightGuideline = wandCopingSkillActivity.findViewById(R.id.rightGuideline);
        float xStart = rightGuideline.getX();
        Display display = wandCopingSkillActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        //Log.e("Width", "" + width);

        //TODO make this read from the actual view?
        handWidth = 303;

        //Log.e("Hand Width", "" + handWidth);
        float xLimit = ((float)width)*((float)0.15);
        float xDist = ((float)width)*((float)0.7) - handWidth;
        /*Log.e(Constants.LOG_TAG, "startWandMoving - x Limit is: " + xLimit);
        Log.e(Constants.LOG_TAG, "startWandMoving - x Start is: " + xStart);
        Log.e(Constants.LOG_TAG, "startWandMoving - x Distance is: " + xDist);
        */
        ImageView wandView = wandCopingSkillActivity.findViewById(R.id.imageViewWandHand);

        AnimationSet wandMov = new AnimationSet(true);
        int repCount = (int)(SONG_DURATION/TEMPO);

        RotateAnimation rot = new RotateAnimation(45, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                1.0f);
        rot.setStartOffset(50);
        rot.setDuration(TEMPO);
        rot.setRepeatCount(repCount);
        rot.setRepeatMode(2);
        wandMov.addAnimation(rot);

        TranslateAnimation animation = new TranslateAnimation(xStart, -xDist, 0.0f, 0.0f);
        //TranslateAnimation animation = new TranslateAnimation(xStart, 0.0f, 0.0f, 0.0f);
        animation.setDuration(TEMPO);
        animation.setRepeatCount(repCount);
        animation.setRepeatMode(2);
        animation.setFillAfter(true);
        wandMov.addAnimation(animation);
        wandView.startAnimation(wandMov);
    }

    public void playSong(){
        // Play the song
        Log.e(Constants.LOG_TAG, "Attempting to play audio");
        wandCopingSkillActivity.playMusic();
        //wandCopingSkillActivity.playAudio(wandCopingSkillActivity.getAudioFileForMusic());
        // Start a timer
        timerToDisplayOverlay.startTimer();
    }

}
