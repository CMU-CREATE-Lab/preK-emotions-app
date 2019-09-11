package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;

public abstract class StaticCopingSkillActivity extends AbstractCopingSkillActivity {

    private static final long DEFAULT_DISPLAY_OVERLAY_AFTER_MILLISECONDS = 30000;
    private static final long DEFAULT_DISMISS_OVERLAY_AFTER_MILLISECONDS = 10000;
    private StaticCopingSkillTimeoutOverlay staticCopingSkillTimeoutOverlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        staticCopingSkillTimeoutOverlay = createTimeoutOverlay();

        findViewById(R.id.activityBackground).setBackgroundResource(getResourceForBackground());
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setText(getTextTitleResource());
        textViewTitle.setTextColor(getResources().getColor(getColorResourceForTitle()));
    }


    @Override
    protected void onPause() {
        super.onPause();
        staticCopingSkillTimeoutOverlay.onPauseActivity();
    }


    @Override
    protected void onResume() {
        super.onResume();
        playAudio(getAudioFileForCopingSkillTitle());
        staticCopingSkillTimeoutOverlay.onResumeActivity();
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_static_coping_skill;
    }


    /** Get the color resource to use for the title (white by default). */
    @ColorRes
    public int getColorResourceForTitle() {
        return R.color.colorWhite;
    }


    public StaticCopingSkillTimeoutOverlay createTimeoutOverlay() {
        return new StaticCopingSkillTimeoutOverlay(this);
    }


    public long getMillisecondsToDisplayOverlay() {
        return DEFAULT_DISPLAY_OVERLAY_AFTER_MILLISECONDS;
    }


    public long getMillisecondsToDismissOverlay() {
        return DEFAULT_DISMISS_OVERLAY_AFTER_MILLISECONDS;
    }


    /**
     * Static coping skills will always have some centered text. This is the audio file associated with that text.
     *
     * @return relative path in assets for the audio file.
     */
    public abstract String getAudioFileForCopingSkillTitle();


    /** Get the background resource for the coping skill. */
    @DrawableRes
    public abstract int getResourceForBackground();


    /** Get the string resource for the text that appears on the coping skill. */
    @StringRes
    public abstract int getTextTitleResource();

}
