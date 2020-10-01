package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand_standalone;

import android.content.Context;
import android.media.AudioManager;
import android.os.CountDownTimer;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;

import java.util.Random;

public class WandStandaloneAudioHandler {
    private WandStandaloneActivity wandStandaloneActivity;
    private WandStandaloneProcess wandStandaloneProcess;
    private AudioPlayer audioPlayer;
    private AudioManager audioManager;
    private boolean settingAudio = false;
    private boolean volumeLow = false;
    private boolean playingSlowAudio = false;
    private int lastVolume = 0;
    private int audioHandlerCount = 0;
    private int audioHandlerMaxCount = 300;  //Time between playing audio to slow down
    private int fastCount = 0;
    private  double fastThreshold = 0.9;
    private long slowAudioDuration = 2000;


    public WandStandaloneAudioHandler (WandStandaloneActivity wandStandaloneActivity, WandStandaloneProcess wandStandaloneProcess) {
        this.wandStandaloneActivity = wandStandaloneActivity;
        this.wandStandaloneProcess = wandStandaloneProcess;
        audioPlayer = AudioPlayer.getInstance(wandStandaloneActivity.getApplicationContext());
        audioManager = (AudioManager)wandStandaloneActivity.getSystemService(Context.AUDIO_SERVICE);
        lastVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public void setAudio(boolean fast) {
        if (!settingAudio) {
            settingAudio = true;


            if (!playingSlowAudio) {
                // Count the times called
                audioHandlerCount++;
                Log.e(Constants.LOG_TAG, "Count: " + audioHandlerCount);

                // Count the fast times
                if (fast) {
                    fastCount++;
                    Log.e(Constants.LOG_TAG, "Fast Count: " + fastCount);
                }
            }

            // Check the amount of fast data points
            if (audioHandlerCount >= audioHandlerMaxCount) {
                if (fastCount >= (int)(fastThreshold*audioHandlerMaxCount)) {
                    playSlowAudio();
                }
                // Reset counts
                audioHandlerCount = 0;
                fastCount = 0;
            } else {
                // Set audio based on speed
                if (fast) {
                    setVolumeLow();
                } else {
                    setVolumeHigh();
                }
            }

            // release mutex
            settingAudio = false;
        }
    }

    public void resetAudio() {
        if (volumeLow) {
            setVolumeHigh();
        }
    }

    private void setVolumeLow() {
        if (!volumeLow && !playingSlowAudio) {
            lastVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int setVol = Math.max(1, lastVolume / 6);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, setVol, AudioManager.FLAG_PLAY_SOUND);
            volumeLow = true;
        }
    }

    private void setVolumeHigh() {
        if(volumeLow) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, lastVolume, AudioManager.FLAG_PLAY_SOUND);
            volumeLow = false;
        }
    }

    private void playSlowAudio() {
        Random rand = new Random();
        String wandAudio = "";
        int n = rand.nextInt(3);
        switch (n) {
            case 0:
                wandAudio = "etc/audio_prompts/audio_wand_try_slower.wav";
                slowAudioDuration = 2300;
                break;
            case 1:
                wandAudio = "etc/audio_prompts/audio_wand_slow_down.wav";
                slowAudioDuration = 1000;
                break;
            case 2:
                wandAudio = "etc/audio_prompts/audio_wand_move_slowly.wav";
                slowAudioDuration = 2000;
                break;
            default:
                wandAudio = "etc/audio_prompts/audio_wand_try_slower.wav";
                slowAudioDuration = 2000;
                break;
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, lastVolume, AudioManager.FLAG_PLAY_SOUND);
        volumeLow = false;
        audioPlayer.stop();
        wandStandaloneActivity.playAudio(wandAudio);
        playingSlowAudio = true;
        new CountDownTimer(slowAudioDuration, 100) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                playingSlowAudio = false;
            }
        }.start();
        wandStandaloneProcess.playSong();
    }
}