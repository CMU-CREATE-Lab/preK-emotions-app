package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand;


import android.Manifest;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_static.StaticCopingSkillTimeoutOverlay;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class WandCopingSkillActivity extends AbstractCopingSkillActivity {

    private boolean activityIsPaused = false;
    private WandStateHandler wandStateHandler;
    private WandCopingSkillProcess wandCopingSkillProcess;
    private boolean volumeLow = false;
    private int lastVolume = 0;
    private int delay = 500;
    private Thread t;
    private static volatile boolean running;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AudioPlayer.getInstance(getApplicationContext()).stop();
        AudioPlayer.getInstance(getApplicationContext()).addAudioFromAssets(getAudioFileForCopingSkillTitle());
        AudioPlayer.getInstance(getApplicationContext()).playAudio();

        wandCopingSkillProcess = new WandCopingSkillProcess(this);

        setScreen();
        wandCopingSkillProcess.startWandMoving();

        findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        wandStateHandler = new WandStateHandler(this);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        running = true;

        t = new Thread() {
            @Override
            public void run() {
                while (running) {
                    try {
                        Thread.sleep(delay);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wandStateHandler.update();
                            }
                        });
                        if(Thread.interrupted()) {
                            return;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }


    @Override
    protected void onPause() {
        activityIsPaused = true;
        Log.d(Constants.LOG_TAG,"Stopping Scan...");
        wandStateHandler.pauseState();
        wandCopingSkillProcess.onPauseActivity();
        super.onPause();
    }


    @Override
    protected void onResume() {
        activityIsPaused = false;
        wandStateHandler.initializeState();
        wandStateHandler.lookForWand();
        playAudio(getAudioFileForCopingSkillTitle());
        wandCopingSkillProcess.onResumeActivity();
        wandCopingSkillProcess.playSong();
        super.onResume();
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_wand_coping_skill;
    }


    /** Get the color resource to use for the title (white by default). */
    @ColorRes
    public int getColorResourceForTitle() {
        return R.color.colorWhite;
    }

    public String getAudioFileForCopingSkillTitle() {
        return "etc/audio_prompts/audio_wand.wav";
    }

    public String getAudioFileForMusic() {
        return "etc/music/WandMusic.wav";
    }

    public void playMusic(){
        AudioPlayer.getInstance(getApplicationContext()).addAudioFromAssets(getAudioFileForMusic());
        //AudioPlayer.getInstance(getApplicationContext()).playAudio();
    }

    public void stopMusic () {
        AudioPlayer.getInstance(getApplicationContext()).stop();
    }

    public void setVolumeLow() {
        AudioManager audioManager =
                (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        if(!volumeLow) {
            lastVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            volumeLow = true;
            int setVol = Math.max(1, lastVolume / 6);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, setVol, AudioManager.FLAG_PLAY_SOUND);
        }
    }

    public void setVolumeHigh() {
        if(volumeLow) {
            AudioManager audioManager =
                    (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, lastVolume, AudioManager.FLAG_PLAY_SOUND);
        }

        volumeLow = false;
    }

    /** Get the background resource for the coping skill. */
    @DrawableRes
    public int getResourceForBackground() {
        return R.drawable.background_wand;
    }


    /** Get the string resource for the text that appears on the coping skill. */
    @StringRes
    public int getTextTitleResource() {
        return R.string.coping_skill_wand;
    }

    public boolean isPaused() {
        return activityIsPaused;
    }

    public void setScreen() {
        findViewById(R.id.activityBackground).setBackgroundResource(getResourceForBackground());
        TextView textView = findViewById(R.id.textViewTitle);
        textView.setText(getTextTitleResource());
        textView.setTextColor(getResources().getColor(getColorResourceForTitle()));
    }

    public void finish(){
        wandStateHandler.pauseState();
        // TODO set volume to original
        if(volumeLow) {
            AudioManager audioManager =
                    (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, lastVolume, AudioManager.FLAG_PLAY_SOUND);
        }

        t.interrupt();
        running = false;

        super.finish();
    }
}