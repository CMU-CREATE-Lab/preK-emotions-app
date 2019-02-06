package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;

import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;
import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.audio.audio_recording.AudioRecorder;

public class RecordUseWordsActivity extends PostCopingSkillActivity {

    private static final long MAXIMUM_RECORD_LENGTH_MILLISECONDS = 20000;
    // NOTE: the visual portion of the animation is roughly 3/5 of the actual duration of the animation, so this value should be roughly 2/3 of the value above
    private static final long ANIMATION_OFFSET_IN_MILLISECONDS = 12000;

    private AudioRecorder audioRecorder;
    private final BackgroundTimer timerToStopRecording = new BackgroundTimer(MAXIMUM_RECORD_LENGTH_MILLISECONDS, new BackgroundTimer.TimeExpireListener() {
        @Override
        public void timerExpired() {
            stopRecordingAndMoveOn();
        }
    });

    private boolean activityIsCancelled = false;
    private boolean layoutAnimationIsReady = false;
    private boolean recordButtonPressed = false;
    private boolean startedRecording = false;

    private View viewForCircleAnimation;
    private View layoutCircles, layoutRecordButton;


    private void stopRecording() {
        audioRecorder.stopRecording();
    }


    private void circleRevealAnimation(View myView) {
        // Check if the runtime version is at least Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            int cx = myView.getWidth() / 2;
            int cy = myView.getHeight() / 2;

            // get the final radius for the clipping circle
            float finalRadius = (float) Math.hypot(cx, cy);

            // create the animator for this view (the start radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0f, finalRadius);
            anim.setDuration(MAXIMUM_RECORD_LENGTH_MILLISECONDS+ANIMATION_OFFSET_IN_MILLISECONDS);

            // make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            // set the view to visible without a circular reveal animation below Lollipop
            myView.setVisibility(View.VISIBLE);
        }
    }


    private void goToNextPostCopingSkillActivity() {
        Intent intent = new Intent(this, MoveOnUseWordsActivity.class);
        startActivity(intent);
    }


    public void checkToBeginRecording() {
        if (activityIsCancelled) {
            Log.w(Constants.LOG_TAG, "checkToBeginRecording but activityIsCancelled is true; returning");
            return;
        } else if (startedRecording) {
            Log.w(Constants.LOG_TAG, "checkToBeginRecording but startedRecording is true; returning");
            return;
        } else if (layoutAnimationIsReady && recordButtonPressed) {
            startedRecording = true;

            // be certain that audio won't play when you start recording
            AudioPlayer.getInstance(getApplicationContext()).stop();

            audioRecorder.startRecording();
            circleRevealAnimation(viewForCircleAnimation);
            timerToStopRecording.startTimer();
        }
    }


    public void stopRecordingAndMoveOn() {
        timerToStopRecording.stopTimer();
        stopRecording();
        goToNextPostCopingSkillActivity();
    }


    @Override
    public String getAudioFileForPostCopingSkillTitle() {
        return "etc/audio_prompts/audio_record_press_button.wav";
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_record;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioRecorder = new AudioRecorder(getApplicationContext());

        // assign views
        this.viewForCircleAnimation = findViewById(R.id.imageViewCircleGreen);
        this.layoutCircles = findViewById(R.id.layoutCircles);
        this.layoutRecordButton = findViewById(R.id.layoutRecordButton);

        // previously invisible view
        viewForCircleAnimation.setVisibility(View.INVISIBLE);

        viewForCircleAnimation.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                layoutAnimationIsReady = true;
                checkToBeginRecording();
            }
        });

        findViewById(R.id.imageViewStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecordingAndMoveOn();
            }
        });

        findViewById(R.id.imageViewRecordButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordButtonPressed = true;
                layoutRecordButton.setVisibility(View.GONE);
                layoutCircles.setVisibility(View.VISIBLE);
                // avoid timeout overlay when you press to start recording
                releaseOverlayTimers();
                checkToBeginRecording();
            }
        });
    }


    // Avoid recording while in background
    @Override
    protected void onPause() {
        activityIsCancelled = true;
        timerToStopRecording.stopTimer();
        super.onPause();

        stopRecording();
        finish();
    }

}
