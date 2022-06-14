package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand;

import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;

import java.io.IOException;
import java.util.Random;

public class WandCopingSkillAudioHandler {
    private WandCopingSkillActivity wandCopingSkillActivity;
    private MediaPlayer audioPlayer;
    private boolean settingAudio = false;
    private boolean playingSlowAudio = false;
    private int audioHandlerCount = 0;
    private int audioHandlerMaxCount = 200;  //Time between playing audio to slow down
    private int fastCount = 0;
    private  double fastThreshold = 0.8;
    private long slowAudioDuration = 2000;
    private long titleAduioDuration = 4000;
    private boolean music_playing = false;
    private boolean music_paused = false;
    public boolean title_playing = false;
    public boolean more_time_playing = false;
    private AssetFileDescriptor music;


    public WandCopingSkillAudioHandler (WandCopingSkillActivity wandCopingSkillActivity) {
        this.wandCopingSkillActivity = wandCopingSkillActivity;
        try {
            initMediaPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAudio(boolean fast) {
        if (!settingAudio) {
            settingAudio = true;

            if (title_playing || more_time_playing) {
                settingAudio = false;
                return;
            }

            if (!playingSlowAudio) {
                // Count the times called
                audioHandlerCount++;
                //Log.e(Constants.LOG_TAG, "Count: " + audioHandlerCount);

                // Count the fast times
                if (fast) {
                    fastCount++;
                    //Log.e(Constants.LOG_TAG, "Fast Count: " + fastCount);
                }


                // Check the amount of fast data points
                if (audioHandlerCount >= audioHandlerMaxCount) {
                    if (fastCount >= (int) (fastThreshold * audioHandlerMaxCount)) {
                        playSlowAudio();
                    }
                    // Reset counts
                    audioHandlerCount = 0;
                    fastCount = 0;
                } else {
                    // Set audio based on speed
                    if (fast) {
                        pauseAudio();
                    } else {
                        if (!music_playing) {
                            audioPlayer.start();
                            music_playing = true;
                        }
                    }
                }
            }

            // release mutex
            settingAudio = false;
        }
    }

    public void pauseAudio() {
        if (music_playing) {
            audioPlayer.pause();
            music_playing = false;
        }
    }

    public void stopAudio() {
        audioPlayer.pause();
        music_playing = false;
        audioPlayer.seekTo(0);
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
                slowAudioDuration = 1800;
                break;
            case 2:
                wandAudio = "etc/audio_prompts/audio_wand_move_slowly.wav";
                slowAudioDuration = 2300;
                break;
            default:
                wandAudio = "etc/audio_prompts/audio_wand_try_slower.wav";
                slowAudioDuration = 2300;
                break;
        }
        if (music_playing) {
            pauseAudio();
            music_paused = true;
        }
        playingSlowAudio = true;
        wandCopingSkillActivity.playAudio(wandAudio);
        new CountDownTimer(slowAudioDuration, 100) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                playingSlowAudio = false;
                if (music_paused) {
                    audioPlayer.start();
                    music_playing = true;
                }
            }
        }.start();
    }

    private void initMediaPlayer() throws IOException {
        audioPlayer = new MediaPlayer();
        audioPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        music = wandCopingSkillActivity.getAssets().openFd(wandCopingSkillActivity.getAudioFileForMusic());
        audioPlayer.setDataSource(music.getFileDescriptor(), music.getStartOffset(), music.getLength());
        audioPlayer.prepare();
    }

    public void playedTitle () {
        if (!title_playing) {
            title_playing = true;
            new CountDownTimer(titleAduioDuration, 500) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    title_playing = false;
                }
            }.start();
        }
    }
}
