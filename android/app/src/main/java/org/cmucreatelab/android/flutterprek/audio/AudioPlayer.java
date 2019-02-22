package org.cmucreatelab.android.flutterprek.audio;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.Constants;

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
    private ConcurrentLinkedQueue<AudioFile> audioFilesQueue;

    private enum StorageType {
        ASSET,
        INTERNAL_STORAGE
    }

    private class AudioFile {
        final String filepath;
        final StorageType storageType;
        final MediaPlayer.OnCompletionListener listener;

        AudioFile(String filepath, StorageType storageType, MediaPlayer.OnCompletionListener listener) {
            this.filepath = filepath;
            this.storageType = storageType;
            this.listener = listener;
        }
    }


    private void playNext() throws IOException {
//        // NOTE: this is for resources (we use assets below instead)
//        Uri uri = Uri.parse("android.resource://" + appContext.getPackageName() + "/" + fileIds.poll());
        mediaPlayer.reset();

        final AudioFile audioFileToPlay = audioFilesQueue.poll();
        if (audioFileToPlay.storageType == StorageType.ASSET) {
            // NOTE: setDataSource with AssetFileDescriptor requires API Level 24, so using this offset/length hack is necessary to avoid this.
            // for more info, see this post: https://stackoverflow.com/questions/3289038/play-audio-file-from-the-assets-directory#3389965
            AssetFileDescriptor afd = appContext.getAssets().openFd(audioFileToPlay.filepath);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } else {
            // TODO consider resources as well?
            mediaPlayer.setDataSource(audioFileToPlay.filepath);
        }
        // set custom listener
        if (audioFileToPlay.listener != null) {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // make call to custom listener
                    audioFileToPlay.listener.onCompletion(mp);
                    // reset listener to AudioPlayer class (and make usual callback afterwards)
                    mediaPlayer.setOnCompletionListener(AudioPlayer.this);
                    AudioPlayer.this.onCompletion(mp);
                }
            });
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                AudioPlayer.this.mediaPlayer.start();
            }
        });
        mediaPlayer.prepareAsync();
    }


    private AudioPlayer(Context context) {
        audioFilesQueue = new ConcurrentLinkedQueue<>();
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


    public void addAudioFromAssets(String filepath) {
        audioFilesQueue.add(new AudioFile(filepath, StorageType.ASSET, null));
    }


    public void addAudioFromAssets(String filepath, MediaPlayer.OnCompletionListener listener) {
        audioFilesQueue.add(new AudioFile(filepath, StorageType.ASSET, listener));
    }


    public void addAudioFromInternalStorage(String filepath) {
        audioFilesQueue.add(new AudioFile(filepath, StorageType.INTERNAL_STORAGE, null));
    }


    public void addAudioFromInternalStorage(String filepath, MediaPlayer.OnCompletionListener listener) {
        audioFilesQueue.add(new AudioFile(filepath, StorageType.INTERNAL_STORAGE, listener));
    }


    public void playAudio() {
        if (!audioFilesQueue.isEmpty() && !mediaPlayer.isPlaying()) {
            try {
                playNext();
            } catch (IOException e) {
                Log.e(Constants.LOG_TAG, "file I/O error in playAudio - AudioPlayer.");
            }
        }
    }


    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            audioFilesQueue.clear();
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (!audioFilesQueue.isEmpty()) {
            try {
                playNext();
            } catch (IOException e) {
                Log.e(Constants.LOG_TAG, "file I/O error in onCompletion - AudioPlayer.");
            }
        }
    }

}