package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.video.VideoPlayer;

public abstract class VideoCopingSkillActivity extends AbstractCopingSkillActivity {

    //    private StaticCopingSkillTimeoutOverlay staticCopingSkillTimeoutOverlay;
    private VideoView videoView;

    private final MediaPlayer.OnCompletionListener listener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            // TODO overlay to continue?
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO display overlay after video finishes
        findViewById(R.id.overlayYesNo).setVisibility(View.GONE);

        findViewById(R.id.activityBackground).setBackgroundResource(getResourceForBackground());
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setText(getTextTitleResource());
        textViewTitle.setTextColor(getColorResourceForTitle());

        videoView = findViewById(R.id.videoView);
        VideoPlayer.getInstance(getApplicationContext()).Init(this, videoView, getVideoFileForCopingSkillTitle());
    }


    @Override
    protected void onPause() {
        super.onPause();
        VideoPlayer.getInstance(getApplicationContext()).pause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        VideoPlayer.getInstance(getApplicationContext()).playVideo(useAudioFromVideo(), getListener());
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


    public MediaPlayer.OnCompletionListener getListener() {
        return listener;
    }


    public abstract String getVideoFileForCopingSkillTitle();


    /** Get the background resource for the coping skill. */
    @DrawableRes
    public abstract int getResourceForBackground();


    /** Get the string resource for the text that appears on the coping skill. */
    @StringRes
    public abstract int getTextTitleResource();


    public abstract boolean useAudioFromVideo();

}
