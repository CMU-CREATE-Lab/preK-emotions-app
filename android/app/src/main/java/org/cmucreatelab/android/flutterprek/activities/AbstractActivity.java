package org.cmucreatelab.android.flutterprek.activities;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.video.VideoPlayer;

/**
 * All activities in the project should extend from this class.
 */
public abstract class AbstractActivity extends AppCompatActivity {


    VideoPlayer videoPlayer;

    public void playAudio(String filepath) {
        playAudio(filepath, null);
    }

    public void playAudio(String filepath, MediaPlayer.OnCompletionListener listener) {
        if (filepath != null) {
            AudioPlayer audioPlayer = AudioPlayer.getInstance(getApplicationContext());
            audioPlayer.stop();
            audioPlayer.addAudioFromAssets(filepath, listener);
            audioPlayer.playAudio();
        } else {
            Log.w(Constants.LOG_TAG, "ignoring call to playAudio() with null filepath.");
        }
    }

    /**
     * Hide navigation buttons to make the activity take up the entire screen.
     */
    @Override
    protected void onResume() {
        super.onResume();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(uiOptions);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceIdForActivityLayout());
    }


    /** Makes sure the audio player is stopped before the app goes in the background. */
    @Override
    protected void onPause() {
        Log.i(Constants.LOG_TAG, "onPause");
        AudioPlayer.getInstance(getApplicationContext()).stop();
        super.onPause();
    }


    /** Get the main layout ("R.layout." ...) for the extended Activity class. */
    @LayoutRes
    public abstract int getResourceIdForActivityLayout();

}