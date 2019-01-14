package org.cmucreatelab.android.flutterprek.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.AudioPlayer;
import org.cmucreatelab.android.flutterprek.Constants;

/**
 * All activities in the project should extend from this class.
 */
public abstract class AbstractActivity extends AppCompatActivity {


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