package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand_standalone;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_cuddle.GestureListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WandStandaloneProcess {

    private static final long SONG_DURATION = 30000; //170000
    private static final long TEMPO = 1300;
    private static final long DISMISS_OVERLAY_AFTER_MILLISECONDS = 10000;
    private static final int SPEED_THRESHOLD = 600;
    private BackgroundTimer timerToDisplayOverlay, timerToExitFromOverlay;
    private boolean overlayIsDisplayed = false;
    private final WandStandaloneActivity wandStandaloneActivity;
    private ImageView wandView;
    private VelocityTracker mVelocityTracker = null;
    private TextView debugView;
    private float velocities[];
    private int window = 10;
    private int curVel = 0;

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

    private void checkWandPosition() {
        float yMin = 76 + wandStandaloneActivity.findViewById(R.id.textViewTitle).getHeight();
        yMin = 0;
        int wandX = wandView.getWidth();
        int wandY = wandView.getHeight();
        Display display = wandStandaloneActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int yMax = size.y - wandY;
        float xMax = size.x - wandX;
        if (wandView.getX() < 0) {
            wandView.setX(0);
        } else if (wandView.getX() > xMax ) {
            wandView.setX(xMax);
        }
        if (wandView.getY() < yMin) {
            wandView.setY(yMin);
        } else if (wandView.getY() > yMax) {
            wandView.setY(yMax);
        }
    }

    private float average(float vals[]){
        float sum = 0;
        for (int i = 0; i < vals.length; i++) {
            sum += vals[i];
        }
        return sum/vals.length;
    }

    private void speedHandler(float xVelocity, float yVelocity) {
        debugView.setText("X velocity: " + Float.toString(xVelocity) + "\nY velocity: " + Float.toString(yVelocity));
        xVelocity = Math.abs(xVelocity);
        // add velocity to a rolling window
        if (curVel >= velocities.length) {
            curVel = 0;
        }
        velocities[curVel] = xVelocity;
        curVel++;

        //Get average over 10 motions
        float avgVel = average(velocities);

        //Check speed
        if (avgVel <= SPEED_THRESHOLD) {
            wandStandaloneActivity.setVolumeHigh();
        }
        else if (avgVel > SPEED_THRESHOLD) {
            wandStandaloneActivity.setVolumeLow();
            debugView.setText("X velocity: " + Float.toString(xVelocity) + "\nY velocity: " + Float.toString(yVelocity) + "\nLOW");
        }
    }

    public WandStandaloneProcess(final WandStandaloneActivity wandStandaloneActivity) {
        this.wandStandaloneActivity = wandStandaloneActivity;

        velocities = new float[window];
        for (int i = 0; i < velocities.length; i++) {
            velocities[i] = 0;
        }

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

        wandStandaloneActivity.findViewById(R.id.overlayYesNo).findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wandStandaloneActivity.setScreen();
                wandStandaloneActivity.playAudio(wandStandaloneActivity.getAudioFileForCopingSkillTitle());
                playSong();
                hideOverlay();
            }
        });
        wandStandaloneActivity.findViewById(R.id.overlayYesNo).findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        hideOverlay();

        wandView = wandStandaloneActivity.findViewById(R.id.imageViewWandHand);
        debugView = wandStandaloneActivity.findViewById(R.id.textViewDebug);

        wandView.setOnTouchListener(new View.OnTouchListener()
        {
            PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
            PointF StartPT = new PointF(); // Record Start Position of 'img'

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                int index = event.getActionIndex();
                int pointerId = event.getPointerId(index);
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_MOVE :
                        wandView.setX((int) (StartPT.x + event.getX() - DownPT.x));
                        wandView.setY((int) (StartPT.y + event.getY() - DownPT.y));
                        checkWandPosition();
                        StartPT.set(wandView.getX(), wandView.getY());

                        // Velocity tracking
                        mVelocityTracker.addMovement(event);
                        mVelocityTracker.computeCurrentVelocity(1000);

                        float xVelocity = VelocityTrackerCompat.getXVelocity(mVelocityTracker, pointerId);
                        float yVelocity = VelocityTrackerCompat.getYVelocity(mVelocityTracker, pointerId);
                        speedHandler(xVelocity, yVelocity);
                        break;
                    case MotionEvent.ACTION_DOWN :
                        DownPT.set( event.getX(), event.getY() );
                        StartPT.set(wandView.getX(), wandView.getY() );
                        if (mVelocityTracker == null) {
                            mVelocityTracker = VelocityTracker.obtain();
                        } else {
                            mVelocityTracker.clear();
                        }
                        mVelocityTracker.addMovement(event);
                        break;
                    case MotionEvent.ACTION_UP :
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        // Return a VelocityTracker object back to be re-used by others.
                        mVelocityTracker.recycle();
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
        // Start a timer
        timerToDisplayOverlay.startTimer();
    }

    public void stopSong() {
        wandStandaloneActivity.stopMusic();
        timerToDisplayOverlay.stopTimer();
    }

}
