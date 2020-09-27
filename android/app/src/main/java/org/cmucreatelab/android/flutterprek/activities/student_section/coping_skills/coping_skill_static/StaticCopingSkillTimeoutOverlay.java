package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static;

import android.view.View;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.R;

public class StaticCopingSkillTimeoutOverlay {

    private static final String AUDIO_FILE_PROMPT_MORE_TIME = "etc/audio_prompts/audio_more_time.wav";

    public interface OverlayOptionListener {
        void onClickNo();
        void onClickYes();
    }

    private final StaticCopingSkillActivity activity;
    private final BackgroundTimer timerToDisplayOverlay, timerToExitFromOverlay;
    private final OverlayOptionListener listener;
    private boolean overlayIsDisplayed = false;


    private void releaseTimers() {
        timerToDisplayOverlay.stopTimer();
        timerToExitFromOverlay.stopTimer();
    }


    private void finishActivity() {
        releaseTimers();
        activity.finish();
    }


    private void displayOverlay() {
        timerToDisplayOverlay.stopTimer();
        overlayIsDisplayed = true;
        activity.playAudio(getAudioFileForOverlayPrompt());
        activity.findViewById(R.id.overlayYesNo).setVisibility(View.VISIBLE);
        timerToExitFromOverlay.startTimer();
    }


    private void hideOverlay() {
        releaseTimers();
        overlayIsDisplayed = false;
        activity.findViewById(R.id.overlayYesNo).setVisibility(View.GONE);
        timerToDisplayOverlay.startTimer();
    }


    private void onTimerToDisplayOverlayExpired() {
        displayOverlay();
    }


    private void onTimerToExitFromOverlayExpired() {
        finishActivity();
    }


    public StaticCopingSkillTimeoutOverlay(StaticCopingSkillActivity activity) {
        this(activity, null);
    }


    public StaticCopingSkillTimeoutOverlay(StaticCopingSkillActivity activity, final OverlayOptionListener listener) {
        this.activity = activity;
        this.listener = listener;

        timerToDisplayOverlay = new BackgroundTimer(activity.getMillisecondsToDisplayOverlay(), new BackgroundTimer.TimeExpireListener() {
            @Override
            public void timerExpired() {
                timerToDisplayOverlay.stopTimer();
                onTimerToDisplayOverlayExpired();
            }
        });
        timerToExitFromOverlay = new BackgroundTimer(activity.getMillisecondsToDismissOverlay(), new BackgroundTimer.TimeExpireListener() {
            @Override
            public void timerExpired() {
                timerToExitFromOverlay.stopTimer();
                onTimerToExitFromOverlayExpired();
            }
        });

        activity.findViewById(R.id.overlayYesNo).findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideOverlay();
                if (listener != null) {
                    listener.onClickYes();
                }
            }
        });
        activity.findViewById(R.id.overlayYesNo).findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
                if (listener != null) {
                    listener.onClickNo();
                }
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


    public String getAudioFileForOverlayPrompt() {
        return AUDIO_FILE_PROMPT_MORE_TIME;
    }

}
