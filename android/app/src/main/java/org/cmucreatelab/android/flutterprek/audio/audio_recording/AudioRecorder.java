package org.cmucreatelab.android.flutterprek.audio.audio_recording;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Steve on 5/17/2016.
 * Modified by tasota on 1/21/2019.
 *
 * AudioRecorder
 *
 * A helper class to simplify recording audio.
 *
 */
public class AudioRecorder implements Serializable {

    private Context appContext;
    private MediaRecorder mediaRecorder;
    private File audioFile;
    public boolean isRecording;


    // class methods


    public void startRecording() {
        GlobalHandler globalHandler = GlobalHandler.getInstance(appContext);
        this.audioFile = SaveFileHandler.getOutputMediaFile(appContext, SaveFileHandler.MEDIA_TYPE_AUDIO, globalHandler);
        this.mediaRecorder = new MediaRecorder();
        this.mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        this.mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        this.mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
        this.mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        globalHandler.studentSectionNavigationHandler.recordedAudioFile = audioFile;

        try {
            this.mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Could not prepare Recorder.");
        }

        this.mediaRecorder.start();
        this.isRecording = true;
    }


    public void stopRecording() {
        if (mediaRecorder != null && isRecording) {
            this.mediaRecorder.stop();
            this.mediaRecorder.release();
            this.mediaRecorder = null;
            this.isRecording = false;
        }
    }


    public AudioRecorder(Context appContext) {
        this.appContext = appContext;
        this.isRecording = false;
        this.mediaRecorder = null;
    }

}
