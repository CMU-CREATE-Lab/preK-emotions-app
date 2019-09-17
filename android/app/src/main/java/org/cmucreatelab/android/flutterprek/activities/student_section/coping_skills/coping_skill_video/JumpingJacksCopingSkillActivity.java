package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video;

import android.os.Bundle;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;

public class JumpingJacksCopingSkillActivity extends VideoCopingSkillActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setText(R.string.empty);
        textViewTitle.setTextColor(getResources().getColor(R.color.colorWhite));
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_jumping_jacks_coping_skill;
    }


    @Override
    public String getFilePathForVideo() {
        return "android.resource://" + getPackageName() + "/" + R.raw.jumpingjacks;
    }


    @Override
    public int getResourceForBackground() {
        return R.drawable.background_jumping_jacks;
    }


    @Override
    public boolean useAudioFromVideo() {
        return true;
    }

}
