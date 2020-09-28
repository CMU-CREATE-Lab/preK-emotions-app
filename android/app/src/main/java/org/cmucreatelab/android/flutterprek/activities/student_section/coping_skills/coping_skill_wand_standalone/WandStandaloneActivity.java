package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand_standalone;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.AbstractCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;

public class WandStandaloneActivity extends AbstractCopingSkillActivity {

    private boolean activityIsPaused = false;
    private WandStandaloneProcess wandStandaloneProcess;
    private boolean volumeLow = false;
    private int lastVolume = 0;
    private static volatile boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Play instructions and song
        AudioPlayer.getInstance(getApplicationContext()).stop();
        AudioPlayer.getInstance(getApplicationContext()).addAudioFromAssets(getAudioFileForCopingSkillTitle());
        AudioPlayer.getInstance(getApplicationContext()).playAudio();

        setScreen();

        findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        wandStandaloneProcess = new WandStandaloneProcess(this);
    }

    @Override
    protected void onPause() {
        activityIsPaused = true;
        Log.d(Constants.LOG_TAG,"Stopping Scan...");
        wandStandaloneProcess.onPauseActivity();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityIsPaused = false;
        playAudio(getAudioFileForCopingSkillTitle());
        wandStandaloneProcess.playSong();
        wandStandaloneProcess.onResumeActivity();
    }

    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout.activity_wand_standalone_coping_skill;
    }


    /** Get the color resource to use for the title (white by default). */
    @ColorRes
    public int getColorResourceForTitle() {
        return R.color.colorWhite;
    }

    public String getAudioFileForCopingSkillTitle() {
        return "etc/audio_prompts/audio_wand_standalone_1a_prompt.wav";
    }

    public String getAudioFileForMusic() {
        return "etc/music/WandMusic.wav";
    }

    public void playMusic(){
        AudioPlayer audioPlayer = AudioPlayer.getInstance(getApplicationContext());
        audioPlayer.addAudioFromAssets(getAudioFileForMusic());
    }

    public void stopMusic () {
        AudioPlayer.getInstance(getApplicationContext()).stop();
    }

    public void setVolumeLow() {
        if(!volumeLow) {
            AudioManager audioManager =
                    (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            lastVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            volumeLow = true;
            int setVol = Math.max(1, lastVolume / 6);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, setVol, AudioManager.FLAG_PLAY_SOUND);
        }
        volumeLow = true;
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
        return R.string.coping_skill_wand_standalone;
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
        super.finish();
        if(volumeLow) {
            AudioManager audioManager =
                    (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, lastVolume, AudioManager.FLAG_PLAY_SOUND);
        }
        running = false;
    }
}
