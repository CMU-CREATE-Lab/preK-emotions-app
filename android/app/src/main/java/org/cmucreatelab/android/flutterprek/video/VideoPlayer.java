package org.cmucreatelab.android.flutterprek.video;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.widget.MediaController;
import android.widget.VideoView;
<<<<<<< HEAD
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;

=======
>>>>>>> master

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

<<<<<<< HEAD
    public void playVideo() { initializePlayer();
=======
    public void playVideo(boolean useAudio, final MediaPlayer.OnCompletionListener listener) {
        initializePlayer(useAudio, listener);
>>>>>>> master
    }

    public void pause() {
        appVideoView.pause();
    }

    public void stop() {
        releasePlayer();
    }

<<<<<<< HEAD
    private void initializePlayer() {
=======
    private void initializePlayer(final boolean useAudio, final MediaPlayer.OnCompletionListener listener) {
>>>>>>> master

        appVideoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        // Start playing!
<<<<<<< HEAD
                        //mediaPlayer.setVolume(0f, 0f);
=======
                        if (!useAudio) mediaPlayer.setVolume(0f, 0f);
>>>>>>> master
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
<<<<<<< HEAD
=======
                        listener.onCompletion(mediaPlayer);
>>>>>>> master
                    }
                });
    }

    private void releasePlayer() {
        appVideoView.stopPlayback();
    }

}
