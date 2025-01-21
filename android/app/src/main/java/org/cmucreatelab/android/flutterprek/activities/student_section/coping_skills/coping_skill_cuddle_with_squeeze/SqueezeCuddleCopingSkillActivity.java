package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_cuddle_with_squeeze;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;

public class SqueezeCuddleCopingSkillActivity extends AbstractCopingSkillActivity {

    private static final long DISPLAY_OVERLAY_AFTER_MILLISECONDS = 30000;
    private static final long DISMISS_OVERLAY_AFTER_MILLISECONDS = 120000;

    private static final long HEART_ANIMATION_MILLISECONDS = 2000;

    private boolean activityIsPaused = false;
    private boolean overlayIsDisplayed = false;
    private boolean heartAnimationIsReady = true;
    private SqueezeCuddleStateHandler squeezeCuddleStateHandler;
    private SqueezeCuddleCopingSkillAnimation squeezeCuddleCopingSkillAnimation;

    private final BackgroundTimer timerToDisplayOverlay = new BackgroundTimer(DISPLAY_OVERLAY_AFTER_MILLISECONDS, new BackgroundTimer.TimeExpireListener() {
        @Override
        public void timerExpired() {
            displayOverlay(true);
        }
    });

    private final BackgroundTimer timerToExitFromOverlay = new BackgroundTimer(DISMISS_OVERLAY_AFTER_MILLISECONDS, new BackgroundTimer.TimeExpireListener() {
        @Override
        public void timerExpired() {
            releaseOverlayTimers();
            finish();
        }
    });

    private final BackgroundTimer timerToWaitForNextHeartAnimation = new BackgroundTimer(HEART_ANIMATION_MILLISECONDS, new BackgroundTimer.TimeExpireListener() {
        @Override
        public void timerExpired() {
            heartAnimationIsReady = true;
        }
    });


    private void playAudioOverlay() {
        playAudio("etc/audio_prompts/audio_sheep_overlay.wav");
    }


    public boolean isPaused() {
        return activityIsPaused;
    }


    public void initializeSqueezeCuddleActivity() {
        releaseOverlayTimers();
        displayOverlay(false);
        squeezeCuddleStateHandler.lookForSqueeze();
        timerToDisplayOverlay.startTimer();
        startTimerToRepromptAndPlayAudio("etc/audio_prompts/audio_sheep_hug.wav");
    }


    public void doSqueeze() {
        cancelTimerToReprompt();
        if (heartAnimationIsReady) {
            heartAnimationIsReady = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        squeezeCuddleCopingSkillAnimation.startAnimation();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    timerToWaitForNextHeartAnimation.startTimer();
                }
            });
        }
    }


    public void displayOverlay(final boolean toDisplay) {
        this.overlayIsDisplayed = toDisplay;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (toDisplay) {
                    findViewById(R.id.overlayYesNo).setVisibility(View.VISIBLE);
                    timerToExitFromOverlay.startTimer();
                    playAudioOverlay();
                } else {
                    findViewById(R.id.overlayYesNo).setVisibility(View.GONE);
                }
            }
        });
    }


    public void releaseOverlayTimers() {
        timerToDisplayOverlay.stopTimer();
        timerToExitFromOverlay.stopTimer();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        squeezeCuddleStateHandler = new SqueezeCuddleStateHandler(this);
        squeezeCuddleCopingSkillAnimation = new SqueezeCuddleCopingSkillAnimation(this);

        ((TextView)findViewById(R.id.textViewOverlayTitle)).setText(R.string.coping_skill_sheep_overlay);
        findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeSqueezeCuddleActivity();
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
        releaseOverlayTimers();
        super.onPause();
    }


    @Override
    protected void onResume() {
        activityIsPaused = false;
        if (overlayIsDisplayed) {
            timerToExitFromOverlay.startTimer();
        } else {
            initializeSqueezeCuddleActivity();
        }
        super.onResume();
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_squeeze_cuddle_coping_skill;
    }

}
