package org.cmucreatelab.android.flutterprek.video;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.widget.MediaController;
import android.widget.VideoView;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;


public class VideoPlayer {

    private static VideoPlayer classInstance;

    private static VideoView appVideoView;
    private Context appContext;

    private VideoPlayer(Context context) {
        appContext = context;
    }

    public static VideoPlayer getInstance(Context context) {
        if (classInstance == null) {
            classInstance = new VideoPlayer(context);
        }
        return classInstance;
    }

    public void Init(Activity activity, VideoView videoView, String videoPath) {
        appVideoView = videoView;
        videoView.setVideoPath(videoPath);
        MediaController mc = new MediaController(activity);
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        videoView.setMediaController(mc);
    }

    public void playVideo() { initializePlayer();
    }

    public void pause() {
        appVideoView.pause();
    }

    public void stop() {
        releasePlayer();
    }

    private void initializePlayer() {

        appVideoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        // Start playing!
                        mediaPlayer.setVolume(0f, 0f);
                        appVideoView.start();
                    }
                });

        // Listener for onCompletion() event (runs after media has finished playing).
        appVideoView.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        // Return the video position to the start.
                        appVideoView.seekTo(0);
                    }
                });
    }

    private void releasePlayer() {
        appVideoView.stopPlayback();
    }

}
