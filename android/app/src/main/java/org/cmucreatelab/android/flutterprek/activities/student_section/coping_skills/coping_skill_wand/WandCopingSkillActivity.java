package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand;


import android.Manifest;
import android.os.Bundle;
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
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;

public class WandCopingSkillActivity extends AbstractCopingSkillActivity {

    private boolean activityIsPaused = false;
    private WandStateHandler wandStateHandler;
    private WandCopingSkillProcess wandCopingSkillProcess;
    private WandCopingSkillAudioHandler wandCopingSkillAudioHandler;
    private int delay = 250;
    private Thread t;
    private Thread t_log;
    private static volatile boolean running;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AudioPlayer.getInstance(getApplicationContext()).stop();
        AudioPlayer.getInstance(getApplicationContext()).addAudioFromAssets(getAudioFileForCopingSkillTitle());
        AudioPlayer.getInstance(getApplicationContext()).playAudio();

        wandCopingSkillAudioHandler = new WandCopingSkillAudioHandler(this);
        wandCopingSkillProcess = new WandCopingSkillProcess(this, wandCopingSkillAudioHandler);

        displayTextTitle();
        wandCopingSkillProcess.startWandMoving();

        findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        wandStateHandler = new WandStateHandler(this, wandCopingSkillAudioHandler);

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

        t_log = new Thread() {
            @Override
            public void run() {
                while(running) {
                    try {
                        Thread.sleep(20);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wandStateHandler.logData();
                            }
                        });
                        if (Thread.interrupted()) {
                            return;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t_log.start();
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
        super.onResume();
        activityIsPaused = false;
        wandStateHandler.initializeState();
        playAudio(getAudioFileForCopingSkillTitle());
        wandCopingSkillAudioHandler.playedTitle();
        wandStateHandler.lookForWand();
        wandCopingSkillProcess.onResumeActivity();
    }


    @Override
    public void finish() {
        super.finish();

        wandStateHandler.pauseState();
        wandCopingSkillAudioHandler.stopAudio();
        t.interrupt();
        t_log.interrupt();
        running = false;
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
        return "etc/audio_prompts/audio_wand_slowly.wav";
    }


    public String getAudioFileForMusic() {
        return "etc/music/WandMusic.wav";
    }


    public void playMusic() {
        AudioPlayer audioPlayer = AudioPlayer.getInstance(getApplicationContext());
        audioPlayer.addAudioFromAssets(getAudioFileForMusic());
    }


    public void stopMusic () {
        AudioPlayer.getInstance(getApplicationContext()).stop();
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


    public void displayTextTitle() {
        findViewById(R.id.activityBackground).setBackgroundResource(getResourceForBackground());
        TextView textView = findViewById(R.id.textViewTitle);
        textView.setText(getTextTitleResource());
        textView.setTextColor(getResources().getColor(getColorResourceForTitle()));
    }
}