package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;

import java.util.Timer;
import java.util.TimerTask;


import static org.cmucreatelab.android.flutterprek.R.id.textViewTitle;

public class JumpingJacksCopingSkillActivity extends AbstractCopingSkillActivity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.activityBackground).setBackgroundResource(getResourceForBackground());
        findViewById(R.id.overlayYesNo).setVisibility(View.GONE);

        videoView = findViewById(R.id.videoView);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.jumpingjacks);

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


        MediaController mc = new MediaController(this);
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        videoView.setMediaController(mc);

        final Timer pauseVideo = new Timer();
        final Timer resumeVideo = new Timer();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public final void onPrepared(final MediaPlayer mp) {
                mp.setVolume(0f, 0f);
                mp.start();

                pauseVideo.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mp.pause();

                        resumeVideo.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                mp.start();
                            }
                        }, 1000);
                    }
                }, 5000);


            }
        });

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
