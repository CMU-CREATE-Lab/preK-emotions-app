package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words.fragments.MoveOnFragment;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_rejoin_friends.RejoinFriendsActivity;

import java.io.File;

public class MoveOnUseWordsActivity extends PostCopingSkillActivity {

    private MoveOnFragment moveOnFragment;


    private void goToNextPostCopingSkillActivity(Class nextClass) {
        Intent intent = new Intent(this, nextClass);
        // avoid making new instance of RecordUseWordsActivity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private void addRecordedAudio(boolean playback) {
        // release timers if we are playing back audio
        releaseOverlayTimers();

        File recordedAudioFile = GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.recordedAudioFile;
        if (recordedAudioFile != null) {
            Log.d(Constants.LOG_TAG, "Added audio to play: " + recordedAudioFile.getAbsolutePath());
            AudioPlayer audioPlayer = AudioPlayer.getInstance(getApplicationContext());
            if (playback) {
                audioPlayer.stop();
                audioPlayer.addAudioFromInternalStorage(recordedAudioFile.getAbsolutePath(), this);
                audioPlayer.playAudio();
            } else {
                audioPlayer.addAudioFromInternalStorage(recordedAudioFile.getAbsolutePath());
            }
        }
    }


    @Override
    protected void onResume() {
        // add recorded message first (no playback)
        addRecordedAudio(false);
        // then add prompt (plays recording first, then prompt)
        super.onResume();
    }


    @Override
    public String getAudioFileForPostCopingSkillTitle() {
        return "etc/audio_prompts/audio_move_on.wav";
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._coping_skill__activity_move_on;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.moveOnFragment = (MoveOnFragment) (getSupportFragmentManager().findFragmentById(R.id.moveOnFragment));

        findViewById(R.id.imageViewNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPostCopingSkillActivity(RecordUseWordsActivity.class);
            }
        });
        findViewById(R.id.imageViewYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPostCopingSkillActivity(RejoinFriendsActivity.class);
            }
        });
        findViewById(R.id.layoutPlayButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecordedAudio(true);
            }
        });
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        // restart the timers after audio playback completes
        restartOverlayTimers();
    }

}
