package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;

public abstract class StaticCopingSkillActivity extends AbstractCopingSkillActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.activityBackground).setBackgroundResource(getResourceForBackground());
        ((TextView)findViewById(R.id.textViewTitle)).setText(getTextTitleResource());
    }


    @Override
    protected void onResume() {
        super.onResume();
        playAudio(getAudioFileForCopingSkillTitle());
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_static_coping_skill;
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
