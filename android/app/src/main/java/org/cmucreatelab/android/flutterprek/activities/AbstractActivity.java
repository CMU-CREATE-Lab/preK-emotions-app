package org.cmucreatelab.android.flutterprek.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.android.flutterprek.BackgroundTimer;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;
import org.cmucreatelab.android.flutterprek.Constants;

/**
 * All activities in the project should extend from this class.
 */
public abstract class AbstractActivity extends AppCompatActivity implements BackgroundTimer.TimeExpireListener {

    private final DelayedOnClickHandler delayedOnClickHandler = new DelayedOnClickHandler(this);
    private boolean audioPlaybackPaused = false;
    private BackgroundTimer backgroundTimerForReprompt;
    private boolean backgroundTimerForRepromptIsStarted = false;
    private String audioFileForReprompt;


    /**
     * An activity by default will delay setting onclick listeners for its views.
     *
     * @return false if onclick listeners should be immediately set when the activity resumes.
     */
    public boolean activityUsesDelayedOnClickHandler() {
        return true;
    }


    /**
     * Determines if views in the activity should respond to onclick events. This depends on the state of the DelayedOnClickHandler.
     *
     * @return true if onclick events should be handled, false otherwise.
     */
    public final boolean activityShouldHandleOnClickEvents() {
        if (activityUsesDelayedOnClickHandler()) {
            return delayedOnClickHandler.delayIsFinished();
        }
        return true;
    }


    public synchronized void setAudioPlaybackPaused(boolean value) {
        audioPlaybackPaused = value;
    }


    public boolean isAudioPlaybackPaused() {
        return audioPlaybackPaused;
    }


    /**
     * Plays the audio prompt for the activity as well as starts a timer that triggers a replay of the same audio file, unless cancelled with the cancelTimerToReprompt() method.
     *
     * @param filepathForPrompt path to the audio file (the prompt)
     */
    public synchronized void startTimerToRepromptAndPlayAudio(String filepathForPrompt) {
        // play audio filepathForPrompt
        playAudio(filepathForPrompt);

        // create/start a timer, with callback to play prompt again
        if (backgroundTimerForRepromptIsStarted) {
            backgroundTimerForReprompt.stopTimer();
        } else {
            this.backgroundTimerForRepromptIsStarted = true;
        }
        long millisecondsToWait = GlobalHandler.getSharedPreferences(getApplicationContext()).getLong(Constants.PreferencesKeys.settingsRepromptInMilliseconds, Constants.DEFAULT_REPROMPT_IN_MILLISECONDS);
        this.backgroundTimerForReprompt = new BackgroundTimer(millisecondsToWait, this);
        this.audioFileForReprompt = filepathForPrompt;
        backgroundTimerForReprompt.startTimer();
    }


    /**
     * Stops the timer responsible for replaying the audio prompt, as defined by a previous call to the startTimerToRepromptAndPlayAudio() method.
     *
     * This should be called any time the user interacts with the system (e.g. button click, some tangible interaction like button push)
     *
     * NOTE: using Activity.onUserInteraction triggers for too many things (even just touching the screen without buttons)
     *
     */
    public synchronized void cancelTimerToReprompt() {
        if (backgroundTimerForRepromptIsStarted) {
            this.backgroundTimerForRepromptIsStarted = false;
            backgroundTimerForReprompt.stopTimer();
        }
    }


    @Override
    public void timerExpired() {
        this.backgroundTimerForRepromptIsStarted = false;
        playAudio(audioFileForReprompt);
    }


    /**
     * Hide navigation buttons to make the activity take up the entire screen.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(Constants.LOG_TAG, "AbstractActivity.onResume");
        AudioPlayer.getInstance(getApplicationContext()).stop();
        setAudioPlaybackPaused(false);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(uiOptions);

        if (activityUsesDelayedOnClickHandler()) {
            delayedOnClickHandler.onResumeActivity();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceIdForActivityLayout());
    }


    /** Makes sure the audio player is stopped before the app goes in the background. */
    @Override
    protected void onPause() {
        Log.i(Constants.LOG_TAG, "AbstractActivity.onPause");
        setAudioPlaybackPaused(true);

        AudioPlayer.getInstance(getApplicationContext()).stop();
        if (activityUsesDelayedOnClickHandler()) {
            delayedOnClickHandler.onPauseActivity();
        }

        // NOTE: onPause (leaving the activity) cancels reprompt timer
        cancelTimerToReprompt();

        super.onPause();
    }


    public void stopAudio() {
        AudioPlayer.getInstance(getApplicationContext()).stop();
    }


    public void playAudio(String filepath) {
        playAudio(filepath, null);
    }


    /**
     * Play an audio file from the assets directory. Stops any audio that is playing currently.
     *
     * @param filepath Filepath for the audio asset.
     * @param listener Callback for when the added audio finishes playing
     */
    public void playAudio(String filepath, MediaPlayer.OnCompletionListener listener) {
        // NOTE: use startTimerToRepromptAndPlayAudio() instead, if audio to be played is the "main prompt" of the activity
        AudioPlayer audioPlayer = AudioPlayer.getInstance(getApplicationContext());
        audioPlayer.stop();
        if (isAudioPlaybackPaused()) {
            Log.w(Constants.LOG_TAG, "playAudio called from activity but audioPlaybackPaused is true; not playing audio.");
            return;
        }
        if (filepath != null) {
            audioPlayer.addAudioFromAssets(filepath, listener);
            audioPlayer.playAudio();
        } else {
            Log.w(Constants.LOG_TAG, "ignoring call to playAudio() with null filepath.");
        }
    }


    /** Get the main layout ("R.layout." ...) for the extended Activity class. */
    @LayoutRes
    public abstract int getResourceIdForActivityLayout();

}