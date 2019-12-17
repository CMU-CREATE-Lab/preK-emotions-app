package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand;

import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.R;

public class WandCopingSkillProcess {

    private static final long SONG_DURATION = 30000; //170000
    private static final long TEMPO = 1300;
    private static final long DISMISS_OVERLAY_AFTER_MILLISECONDS = 10000;
    private BackgroundTimer timerToDisplayOverlay, timerToExitFromOverlay;
    private boolean overlayIsDisplayed = false;
    private final WandCopingSkillActivity wandCopingSkillActivity;
    private float handWidth;


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
        stopWandMoving();
        stopSong();
        wandCopingSkillActivity.playAudio("etc/audio_prompts/audio_more_time.wav");
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


    public WandCopingSkillProcess(final WandCopingSkillActivity wandCopingSkillActivity) {
        this.wandCopingSkillActivity = wandCopingSkillActivity;

        timerToDisplayOverlay = new BackgroundTimer(SONG_DURATION, new BackgroundTimer.TimeExpireListener() {
            @Override
            public void timerExpired() {
                onTimerToDisplayOverlayExpired();
            }
        });
        timerToExitFromOverlay = new BackgroundTimer(DISMISS_OVERLAY_AFTER_MILLISECONDS, new BackgroundTimer.TimeExpireListener() {
            @Override
            public void timerExpired() {
                onTimerToExitFromOverlayExpired();
            }
        });

        wandCopingSkillActivity.findViewById(R.id.overlayYesNo).findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Fix this so it starts the activity over again
                wandCopingSkillActivity.displayTextTitle();
                wandCopingSkillActivity.playAudio(wandCopingSkillActivity.getAudioFileForCopingSkillTitle());
                playSong();
                hideOverlay();
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


    public void test() {
        final ImageView wandHand = wandCopingSkillActivity.findViewById(R.id.imageViewWandHand);
        handWidth = wandHand.getWidth();
    }


    public void startWandMoving() {
        // TODO clean up
        Display display = wandCopingSkillActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        //TODO make this read from the actual view?
        handWidth = 303;

        //Log.e("Hand Width", "" + handWidth);
        float xDist = ((float)width)*((float)0.7) - handWidth;
        ImageView wandView = wandCopingSkillActivity.findViewById(R.id.imageViewWandHand);

        AnimationSet wandMov = new AnimationSet(true);
        int repCount = (int)(SONG_DURATION/TEMPO);

        RotateAnimation rot = new RotateAnimation(45, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                1.0f);
        rot.setDuration(TEMPO);
        rot.setRepeatCount(repCount);
        rot.setRepeatMode(2);
        wandMov.addAnimation(rot);

        TranslateAnimation animation = new TranslateAnimation(0.0f, -xDist, 0.0f, 0.0f);
        animation.setDuration(TEMPO);
        animation.setRepeatCount(repCount);
        animation.setRepeatMode(2);
        wandMov.addAnimation(animation);
        wandView.startAnimation(wandMov);
    }

    public void stopWandMoving () {
        ImageView wandView = wandCopingSkillActivity.findViewById(R.id.imageViewWandHand);
        wandView.clearAnimation();
    }


    public void playSong() {
        // Play the song
        wandCopingSkillActivity.playMusic();
        //AudioPlayer.getInstance(wandCopingSkillActivity.getApplicationContext()).playAudio();
        // Start a timer
        timerToDisplayOverlay.startTimer();
    }


    public void stopSong() {
        wandCopingSkillActivity.stopMusic();
        timerToDisplayOverlay.stopTimer();
    }

}
