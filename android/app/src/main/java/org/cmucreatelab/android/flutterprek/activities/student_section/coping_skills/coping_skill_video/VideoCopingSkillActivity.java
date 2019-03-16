package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_video;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.VideoView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.video.VideoPlayer;

public abstract class VideoCopingSkillActivity extends AbstractCopingSkillActivity {

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
        findViewById(R.id.activityBackground).setBackgroundResource(getResourceForBackground());
        videoView = findViewById(R.id.videoView);
        VideoPlayer.getInstance(getApplicationContext()).Init(this, videoView, getFilePathForVideo());

        // TODO display overlay after video finishes
        findViewById(R.id.overlayYesNo).setVisibility(View.GONE);
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


    public MediaPlayer.OnCompletionListener getListener() {
        return listener;
    }


    /** The file path for the video as a string. Note that this is used by {@link VideoView#setVideoPath(String)}. */
    public abstract String getFilePathForVideo();


    /** Get the background resource for the coping skill. */
    public abstract @DrawableRes int getResourceForBackground();


    /** boolean to indicate if the video player should use audio from the video file (true) or mute it (false). */
    public abstract boolean useAudioFromVideo();

}
