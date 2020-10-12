package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand_standalone;

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
    private static volatile boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setScreen();

        findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        wandStandaloneProcess = new WandStandaloneProcess(this);

        // Play instructions and song
        AudioPlayer.getInstance(getApplicationContext()).stop();
        AudioPlayer.getInstance(getApplicationContext()).addAudioFromAssets(getAudioFileForCopingSkillTitle());
        wandStandaloneProcess.playedTitle();
        AudioPlayer.getInstance(getApplicationContext()).playAudio();
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
        wandStandaloneProcess.playedTitle();
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

    public void setScreen() {
        findViewById(R.id.activityBackground).setBackgroundResource(getResourceForBackground());
        TextView textView = findViewById(R.id.textViewTitle);
        textView.setText(getTextTitleResource());
        textView.setTextColor(getResources().getColor(getColorResourceForTitle()));
    }

    public void finish(){
        super.finish();
        running = false;
    }
}
