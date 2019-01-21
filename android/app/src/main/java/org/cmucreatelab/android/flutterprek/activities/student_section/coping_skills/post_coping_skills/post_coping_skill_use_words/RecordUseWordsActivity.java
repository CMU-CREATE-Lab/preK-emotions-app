package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words;

import android.animation.Animator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;

import org.cmucreatelab.android.flutterprek.AudioPlayer;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.audio_recording.AudioRecorder;

public class RecordUseWordsActivity extends PostCopingSkillActivity {

    private static final long MAXIMUM_RECORD_LENGTH_MILLISECONDS = 8000;
    private AudioRecorder audioRecorder;

    private boolean activityIsCancelled = false;
    private boolean layoutAnimationIsReady = false;
    private boolean promptFinishedPlaying = false;
    private boolean startedRecording = false;

    private View viewForCircleAnimation;


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
            anim.setDuration(MAXIMUM_RECORD_LENGTH_MILLISECONDS);

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


    private void checkToBeginRecording() {
        if (activityIsCancelled) {
            Log.w(Constants.LOG_TAG, "checkToBeginRecording but activityIsCancelled is true; returning");
            return;
        } else if (startedRecording) {
            Log.w(Constants.LOG_TAG, "checkToBeginRecording but startedRecording is true; returning");
            return;
        } else if (layoutAnimationIsReady && promptFinishedPlaying) {
            // be certain that audio won't play when you start recording
            AudioPlayer.getInstance(getApplicationContext()).stop();
            startedRecording = true;
            audioRecorder.startRecording();
            circleRevealAnimation(viewForCircleAnimation);
        }
    }


    @Override
    public String getAudioFileForPostCopingSkillTitle() {
        return "etc/audio_prompts/audio_record.wav";
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_record;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioRecorder = new AudioRecorder(getApplicationContext());

        // previously invisible view
        this.viewForCircleAnimation = findViewById(R.id.imageViewCircleWhite);
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
                stopRecording();
                goToNextPostCopingSkillActivity();
            }
        });
    }


    // Avoid recording while in background
    @Override
    protected void onPause() {
        activityIsCancelled = true;
        super.onPause();

        stopRecording();
        finish();
    }


    // waits for prompt to finish playing before recording
    @Override
    public void onCompletion(MediaPlayer mp) {
        promptFinishedPlaying = true;
        checkToBeginRecording();
    }

}
