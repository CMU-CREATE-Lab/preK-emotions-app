package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;

import static org.cmucreatelab.android.flutterprek.R.id.textViewTitle;

public class JumpingJacksCopingSkillActivity extends AbstractCopingSkillActivity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.activityBackground).setBackgroundResource(getResourceForBackground());
        findViewById(R.id.overlayYesNo).setVisibility(View.GONE);

        videoView = findViewById(R.id.videoView);

        initVideo(this, videoView, "android.resource://" + getPackageName() + "/" + R.raw.jumpingjacks);

        TextView textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setText(getTextTitleResource());
        textViewTitle.setTextColor(getColorResourceForTitle());

    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        playAudio(getAudioFileForCopingSkillTitle());

        playVideo();

    }

    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_jumping_jacks_coping_skill;
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
