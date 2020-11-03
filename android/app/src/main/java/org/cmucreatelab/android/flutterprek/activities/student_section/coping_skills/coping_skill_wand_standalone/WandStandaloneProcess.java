package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand_standalone;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.CountDownTimer;
import android.support.v4.view.VelocityTrackerCompat;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.R;


public class WandStandaloneProcess {

    private static final long SONG_DURATION = 30000;
    private static final long DISMISS_OVERLAY_AFTER_MILLISECONDS = 10000;
    private static final int SPEED_THRESHOLD = 400;
    private static final int MIN_SPEED = 20;
    private BackgroundTimer timerToDisplayOverlay, timerToExitFromOverlay;
    private boolean overlayIsDisplayed = false;
    private final WandStandaloneActivity wandStandaloneActivity;
    public WandStandaloneAudioHandler wandStandaloneAudioHandler;
    private ImageView wandView;
    private VelocityTracker mVelocityTracker = null;
    private TextView debugView;
    private float velocities[];
    private int window = 10;
    private int curVel = 0;


    private void releaseTimers() {
        timerToDisplayOverlay.stopTimer();
        timerToExitFromOverlay.stopTimer();
    }

    private void finishActivity() {
        releaseTimers();
        wandStandaloneAudioHandler.stopAudio();
        wandStandaloneActivity.finish();
    }

    private void displayOverlay() {
        timerToDisplayOverlay.stopTimer();
        overlayIsDisplayed = true;
        ((TextView)wandStandaloneActivity.findViewById(R.id.textViewOverlayTitle)).setText(R.string.coping_skill_wand_standalone_overlay);
        wandStandaloneActivity.findViewById(R.id.overlayYesNo).setVisibility(View.VISIBLE);
        wandStandaloneAudioHandler.stopAudio();
        wandStandaloneActivity.playAudio("etc/audio_prompts/audio_wand_standalone_1b_overlay.wav");
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
        int yMin = 0;

        // get wand size
        int wandX = wandView.getWidth();
        int wandY = wandView.getHeight();

        // Get screen size
        Display display = wandStandaloneActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // Find max x, y position
        int yMax = size.y - wandY;
        float xMax = size.x - wandX;

        // Set wand position to the limits if it's past the limits
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
        if (!overlayIsDisplayed) {
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
            boolean fast = false;
            if (avgVel <= SPEED_THRESHOLD && avgVel >= MIN_SPEED) {
                fast = false;
            } else if (avgVel > SPEED_THRESHOLD) {
                fast = true;
            } else {
                wandStandaloneAudioHandler.pauseAudio();
            }
            wandStandaloneAudioHandler.setAudio(fast);
        }
    }

    public WandStandaloneProcess(final WandStandaloneActivity wandStandaloneActivity) {
        this.wandStandaloneActivity = wandStandaloneActivity;
        wandStandaloneAudioHandler = new WandStandaloneAudioHandler(this.wandStandaloneActivity);

        // Initialize the rolling window for velocities
        velocities = new float[window];
        for (int i = 0; i < velocities.length; i++) {
            velocities[i] = 0;
        }

        // Initialize timers
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

        // Overlay
        wandStandaloneActivity.findViewById(R.id.overlayYesNo).findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wandStandaloneActivity.setScreen();
                //wandStandaloneActivity.playAudio(wandStandaloneActivity.getAudioFileForCopingSkillTitle());
                //playedTitle();
                //playSong();
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

        // Initialize the image views
        wandView = wandStandaloneActivity.findViewById(R.id.imageViewWandHand);
        debugView = wandStandaloneActivity.findViewById(R.id.textViewDebug);
        debugView.setVisibility(View.VISIBLE);

        //Set wand motion
        wandView.setOnTouchListener(new View.OnTouchListener()
        {
            PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
            PointF StartPT = new PointF(); // Record Start Position of wand

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                int index = event.getActionIndex();
                int pointerId = event.getPointerId(index);
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_MOVE :
                        // Set the position to follow the touch event
                        wandView.setX((int) (StartPT.x + event.getX() - DownPT.x));
                        wandView.setY((int) (StartPT.y + event.getY() - DownPT.y));
                        checkWandPosition();
                        // set the new start position
                        StartPT.set(wandView.getX(), wandView.getY());

                        // Velocity tracking
                        mVelocityTracker.addMovement(event);
                        mVelocityTracker.computeCurrentVelocity(1000);
                        float xVelocity = VelocityTrackerCompat.getXVelocity(mVelocityTracker, pointerId);
                        float yVelocity = VelocityTrackerCompat.getYVelocity(mVelocityTracker, pointerId);
                        speedHandler(xVelocity, yVelocity);
                        break;
                    case MotionEvent.ACTION_DOWN :
                        // Set new touch point and start point
                        DownPT.set( event.getX(), event.getY() );
                        StartPT.set(wandView.getX(), wandView.getY() );

                        // Initialize the velocity tracker
                        if (mVelocityTracker == null) {
                            mVelocityTracker = VelocityTracker.obtain();
                        } else {
                            mVelocityTracker.clear();
                        }
                        mVelocityTracker.addMovement(event);

                        break;
                    case MotionEvent.ACTION_UP :
                        wandStandaloneAudioHandler.pauseAudio();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        // Return a VelocityTracker object back to be re-used
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

    public void playedTitle() {
        wandStandaloneAudioHandler.playedTitle();
    }

}
