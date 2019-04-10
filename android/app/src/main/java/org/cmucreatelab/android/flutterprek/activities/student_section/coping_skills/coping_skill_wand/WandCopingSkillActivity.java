package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand;


import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.StaticCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.StaticCopingSkillTimeoutOverlay;

public class WandCopingSkillActivity extends AbstractCopingSkillActivity {

    private StaticCopingSkillTimeoutOverlay staticCopingSkillTimeoutOverlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        staticCopingSkillTimeoutOverlay = new StaticCopingSkillTimeoutOverlay(this);

        //findViewById(R.id.activityBackground).setBackgroundResource(getResourceForBackground());
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

}