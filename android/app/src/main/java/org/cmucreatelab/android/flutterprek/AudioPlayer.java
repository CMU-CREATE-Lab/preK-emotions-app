package org.cmucreatelab.android.flutterprek;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Steve on 5/20/2016.
 * Modified by tasota on 1/13/2019.
 *
 * AudioPlayer
 *
 * A helper class to simplify playing audio clips.
 *
 */
public class AudioPlayer implements MediaPlayer.OnCompletionListener {

    private static AudioPlayer classInstance;
    private Context appContext;
    public final MediaPlayer mediaPlayer;
    private ConcurrentLinkedQueue<String> relativeFilePaths;
//    private ConcurrentLinkedQueue<Integer> fileIds;


    private void playNext() throws IOException {
//        // NOTE: this is for resources (we use assets below instead)
//        Uri uri = Uri.parse("android.resource://" + appContext.getPackageName() + "/" + fileIds.poll());
        mediaPlayer.reset();
        // NOTE: setDataSource with AssetFileDescriptor requires API Level 24, so using this offset/length hack is necessary to avoid this.
        // for more info, see this post: https://stackoverflow.com/questions/3289038/play-audio-file-from-the-assets-directory#3389965
        AssetFileDescriptor afd = appContext.getAssets().openFd(relativeFilePaths.poll());
        mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                AudioPlayer.this.mediaPlayer.start();
            }
        });
        mediaPlayer.prepareAsync();
    }


    private AudioPlayer(Context context) {
//        fileIds = new ConcurrentLinkedQueue<>();
        relativeFilePaths = new ConcurrentLinkedQueue<>();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(this);
        appContext = context;
    }


    public static AudioPlayer getInstance(Context context) {
        if (classInstance == null) {
            classInstance = new AudioPlayer(context);
        }
        return classInstance;
    }


//    public void addAudio(Integer fileId) {
//        fileIds.add(fileId);
//    }
    public void addAudio(String filepath) {
        relativeFilePaths.add(filepath);
    }


    public void playAudio() {
        if (!relativeFilePaths.isEmpty() && !mediaPlayer.isPlaying()) {
            try {
                playNext();
            } catch (IOException e) {
                Log.e(Constants.LOG_TAG, "file I/O error in playAudio - AudioPlayer.");
            }
        }
    }


    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            relativeFilePaths.clear();
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (!relativeFilePaths.isEmpty()) {
            try {
                playNext();
            } catch (IOException e) {
                Log.e(Constants.LOG_TAG, "file I/O error in onCompletion - AudioPlayer.");
            }
        }
    }

}