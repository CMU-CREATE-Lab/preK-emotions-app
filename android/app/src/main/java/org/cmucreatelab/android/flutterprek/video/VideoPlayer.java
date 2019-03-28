package org.cmucreatelab.android.flutterprek.video;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayer {

    private static VideoView appVideoView;
    private Context appContext;


    private void initializePlayer(final boolean useAudio, final MediaPlayer.OnCompletionListener listener) {
        appVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // Start playing!
                if (!useAudio) mediaPlayer.setVolume(0f, 0f);
                appVideoView.start();
            }
        });
        appVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                // Return the video position to the start.
                appVideoView.seekTo(0);
                listener.onCompletion(mediaPlayer);
            }
        });
    }


    private void releasePlayer() {
        appVideoView.stopPlayback();
    }


    // Singleton Implementation


    private static VideoPlayer classInstance;


    private VideoPlayer(Context context) {
        appContext = context;
    }


    public static VideoPlayer getInstance(Context context) {
        if (classInstance == null) {
            classInstance = new VideoPlayer(context);
        }
        return classInstance;
    }


    // public methods


    public void prepareViewWithVideo(Activity activity, VideoView videoView, String videoPath) {
        appVideoView = videoView;
        videoView.setVideoPath(videoPath);
        MediaController mc = new MediaController(activity);
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        videoView.setMediaController(mc);
    }


    public void playVideo(boolean useAudio, final MediaPlayer.OnCompletionListener listener) {
        initializePlayer(useAudio, listener);
    }


    public void pause() {
        appVideoView.pause();
    }


    public void stop() {
        releasePlayer();
    }

}
