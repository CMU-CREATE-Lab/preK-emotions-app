package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand;


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
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.StaticCopingSkillTimeoutOverlay;

public class WandCopingSkillActivity extends AbstractCopingSkillActivity {

    private StaticCopingSkillTimeoutOverlay staticCopingSkillTimeoutOverlay;
    private boolean activityIsPaused = false;
    private WandStateHandler wandStateHandler;
    private WandCopingSkillProcess wandCopingSkillProcess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO Needed?
        staticCopingSkillTimeoutOverlay = new StaticCopingSkillTimeoutOverlay(this);
        //wandCopingSkillProcess = new WandCopingSkillProcess(this);

        findViewById(R.id.activityBackground).setBackgroundResource(getResourceForBackground());
        TextView textView = findViewById(R.id.textViewTitle);
        textView.setText(getTextTitleResource());
        textView.setTextColor(getResources().getColor(getColorResourceForTitle()));


        findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wandStateHandler.initializeState();
            }
        });
        findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        wandStateHandler = new WandStateHandler(this);
    }


    @Override
    protected void onPause() {
        activityIsPaused = true;
        super.onPause();
        Log.d(Constants.LOG_TAG,"Stopping Scan...");
        // avoid playing through after early exit
        wandStateHandler.pauseState();
        staticCopingSkillTimeoutOverlay.onPauseActivity();
    }


    @Override
    protected void onResume() {
        activityIsPaused = false;
        super.onResume();
        wandStateHandler.initializeState();
        wandStateHandler.lookForWand();
        playAudio(getAudioFileForCopingSkillTitle());
        // TODO Needed?
        staticCopingSkillTimeoutOverlay.onResumeActivity();
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_wand_coping_skill;
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
        return "etc/audio_prompts/audio_wand.wav";
    }


    /** Get the background resource for the coping skill. */
    @DrawableRes
    public int getResourceForBackground() {
        return R.drawable.background_wand;
    }


    /** Get the string resource for the text that appears on the coping skill. */
    @StringRes
    public int getTextTitleResource() {
        return R.string.coping_skill_wand;
    }

    public boolean isPaused() {
        return activityIsPaused;
    }

}