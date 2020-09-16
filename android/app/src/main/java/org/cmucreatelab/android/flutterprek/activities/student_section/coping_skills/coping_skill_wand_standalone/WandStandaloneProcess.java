package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand_standalone;

import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_cuddle.GestureListener;

public class WandStandaloneProcess {

    private static final long SONG_DURATION = 30000; //170000
    private static final long TEMPO = 1300;
    private static final long DISMISS_OVERLAY_AFTER_MILLISECONDS = 10000;
    private BackgroundTimer timerToDisplayOverlay, timerToExitFromOverlay;
    private boolean overlayIsDisplayed = false;
    private final WandStandaloneActivity wandStandaloneActivity;
    private ImageView wandView;
    private GestureDetector gdt;

    float handWidth;

    private void releaseTimers() {
        timerToDisplayOverlay.stopTimer();
        timerToExitFromOverlay.stopTimer();
    }

    private void finishActivity() {
        releaseTimers();
        wandStandaloneActivity.finish();
    }

    private void displayOverlay() {
        timerToDisplayOverlay.stopTimer();
        overlayIsDisplayed = true;
        wandStandaloneActivity.findViewById(R.id.overlayYesNo).setVisibility(View.VISIBLE);
        stopSong();
        wandStandaloneActivity.playAudio("etc/audio_prompts/audio_more_time.wav");
        timerToExitFromOverlay.startTimer();
    }

    private void hideOverlay() {
        releaseTimers();
        overlayIsDisplayed = false;
        wandStandaloneActivity.findViewById(R.id.overlayYesNo).setVisibility(View.GONE);
        timerToDisplayOverlay.startTimer();
    }

    private void onTimerToDisplayOverlayExpired() {
        displayOverlay();
    }

    private void onTimerToExitFromOverlayExpired() {
        finishActivity();
    }

    public WandStandaloneProcess(final WandStandaloneActivity wandCopingSkillActivity) {
        this.wandStandaloneActivity = wandCopingSkillActivity;

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
                wandCopingSkillActivity.setScreen();
                wandCopingSkillActivity.playAudio(wandCopingSkillActivity.getAudioFileForCopingSkillTitle());
                playSong();
                hideOverlay();
            }
        });
        wandCopingSkillActivity.findViewById(R.id.overlayYesNo).findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        hideOverlay();

        wandView = wandCopingSkillActivity.findViewById(R.id.imageViewWandHand);

        gdt = new GestureDetector(new WandStandaloneGestureListener(wandStandaloneActivity));

        wandView.setOnTouchListener(new View.OnTouchListener()
        {
            PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
            PointF StartPT = new PointF(); // Record Start Position of 'img'

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                gdt.onTouchEvent(event);
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_MOVE :
                        wandView.setX((int)(StartPT.x + event.getX() - DownPT.x));
                        wandView.setY((int)(StartPT.y + event.getY() - DownPT.y));
                        StartPT.set(wandView.getX(), wandView.getY() );
                        break;
                    case MotionEvent.ACTION_DOWN :
                        DownPT.set( event.getX(), event.getY() );
                        StartPT.set(wandView.getX(), wandView.getY() );
                        break;
                    case MotionEvent.ACTION_UP :
                        // Nothing have to do
                        break;
                    default :
                        break;
                }
                return true;
            }
        });


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

    public void playSong(){
        // Play the song
        wandStandaloneActivity.playMusic();
        //AudioPlayer.getInstance(wandCopingSkillActivity.getApplicationContext()).playAudio();
        // Start a timer
        timerToDisplayOverlay.startTimer();
    }

    public void stopSong() {
        wandStandaloneActivity.stopMusic();
        timerToDisplayOverlay.stopTimer();
    }

}
