package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_cuddle;

import android.os.Bundle;
import android.widget.TextView;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.StaticCopingSkillActivity;

public class CuddleCopingSkillActivity extends StaticCopingSkillActivity {

    private static final long DISPLAY_OVERLAY_AFTER_MILLISECONDS = 30000;
    private CuddleCopingSkillAnimation cuddleCopingSkillAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cuddleCopingSkillAnimation = new CuddleCopingSkillAnimation(this);
        TextView overlayText = findViewById(R.id.overlayYesNo).findViewById(R.id.textViewOverlayTitle);
        overlayText.setText(R.string.coping_skill_cuddle_no_manipulative_overlay);
    }

    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_cuddle_coping_skill;
    }


    @Override
    public String getAudioFileForCopingSkillTitle() {
        return "etc/audio_prompts/audio_cuddle.wav";
    }


    @Override
    public int getResourceForBackground() {
        return R.drawable.sheep_background;
    }


    @Override
    public int getTextTitleResource() {
        return R.string.coping_skill_cuddle_no_manipulative;
    }


    @Override
    public long getMillisecondsToDisplayOverlay() {
        return DISPLAY_OVERLAY_AFTER_MILLISECONDS;
    }

}
