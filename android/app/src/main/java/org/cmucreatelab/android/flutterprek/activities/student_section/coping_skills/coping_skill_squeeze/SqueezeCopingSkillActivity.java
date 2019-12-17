package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_squeeze;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;

public class SqueezeCopingSkillActivity extends AbstractCopingSkillActivity {

    // Value of true indicates that we are using new squeeze that only sends a "1" when squeeze is active and "0" otherwise.
    public static final boolean squeezeDetectionIsBinary = true;

    private boolean activityIsPaused = false;
    private SqueezeStateHandler squeezeStateHandler;
    private SqueezeCopingSkillProcess squeezeCopingSkillProcess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        squeezeCopingSkillProcess = new SqueezeCopingSkillProcess(this);
        displayTextTitle();

        findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // do not show the gradient when we are using binary squeeze detection
        if (squeezeDetectionIsBinary) {
            findViewById(R.id.gradient).setVisibility(View.INVISIBLE);
            findViewById(R.id.gradient_pointer).setVisibility(View.INVISIBLE);
        }

        squeezeStateHandler = new SqueezeStateHandler(this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                squeezeStateHandler.initializeSqueezeAnimation();
            }
        });
    }


    @Override
    protected void onPause() {
        activityIsPaused = true;
        Log.d(Constants.LOG_TAG,"Stopping Scan...");
        squeezeStateHandler.pauseState();
        squeezeCopingSkillProcess.onPauseActivity();
        super.onPause();
    }


    @Override
    protected void onResume() {
        activityIsPaused = false;
        squeezeStateHandler.initializeState();
        squeezeStateHandler.lookForSqueeze();
        playAudio(getAudioFileForCopingSkillTitle());
        squeezeCopingSkillProcess.onResumeActivity();
        super.onResume();
    }


    // TODO is this necessary if pauseState() is called in onPause()?
    @Override
    public void finish() {
        squeezeStateHandler.pauseState();
        super.finish();
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_squeeze_coping_skill;
    }


    /** Get the color resource to use for the title (white by default). */
    @ColorRes
    public int getColorResourceForTitle() {
        return R.color.colorWhite;
    }


    /**
     * Static coping skills will always have some centered text. This is the audio file associated with that text.
     *
     * @return relative path in assets for the audio file.
     */
    public String getAudioFileForCopingSkillTitle() {
        return "etc/audio_prompts/audio_squeeze.wav";
    }


    /** Get the background resource for the coping skill. */
    @DrawableRes
    public int getResourceForBackground() {
        return R.drawable.background_squeeze;
    }


    /** Get the string resource for the text that appears on the coping skill. */
    @StringRes
    public int getTextTitleResource() {
        return R.string.coping_skill_squeeze;
    }


    // TODO refactor as a toggle method (display/hide)
    public void displayTextTitle() {
        TextView textView = findViewById(R.id.textViewTitle);
        textView.setText(getTextTitleResource());
        textView.setTextColor(getResources().getColor(getColorResourceForTitle()));
    }


    public boolean isPaused() {
        return activityIsPaused;
    }


    public void releaseTimers() {
        squeezeCopingSkillProcess.releaseTimers();
    }


    public void resetTimers() {
        squeezeCopingSkillProcess.resetTimers();
    }


    public SqueezeStateHandler.State getCurrentState() {
        return squeezeStateHandler.getCurrentState();
    }

}