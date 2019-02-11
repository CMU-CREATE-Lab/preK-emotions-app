package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.StaticCopingSkillTimeoutOverlay;

public class JumpingJacksCopingSkillActivity extends AbstractCopingSkillActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.activityBackground).setBackgroundResource(getResourceForBackground());
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setText(getTextTitleResource());
        textViewTitle.setTextColor(getColorResourceForTitle());
    }


    @Override
    protected void onPause() {
        super.onPause();
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


    /** Get the color resource to use for the title (white by default). */
    @ColorRes
    public int getColorResourceForTitle() {
        return R.color.colorWhite;
    }

    
    public String getAudioFileForCopingSkillTitle() {
        return "etc/audio_prompts/audio_jumping_jacks.wav";
    }


    public int getResourceForBackground() {
        return R.drawable.background_jumping_jacks;
    }


    public int getTextTitleResource() {
        return R.string.coping_skill_jumping_jacks;
    }

}
