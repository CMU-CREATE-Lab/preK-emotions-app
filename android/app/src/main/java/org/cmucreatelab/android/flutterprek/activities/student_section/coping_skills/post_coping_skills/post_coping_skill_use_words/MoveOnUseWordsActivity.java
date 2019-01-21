package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_use_words;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.android.flutterprek.AudioPlayer;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.PostCopingSkillActivity;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.post_coping_skills.post_coping_skill_rejoin_friends.RejoinFriendsActivity;

import java.io.File;

public class MoveOnUseWordsActivity extends PostCopingSkillActivity {


    private void goToNextPostCopingSkillActivity(Class nextClass) {
        Intent intent = new Intent(this, nextClass);
        // avoid making new instance of RecordUseWordsActivity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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

        // TODO playback before "move on" prompt
        File recordedAudioFile = GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.recordedAudioFile;
        if (recordedAudioFile != null) {
            Log.d(Constants.LOG_TAG, "Added audio to play: " + recordedAudioFile.getPath());
            AudioPlayer audioPlayer = AudioPlayer.getInstance(getApplicationContext());
            audioPlayer.stop();
            try {
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(recordedAudioFile.getAbsolutePath());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //playAudio(recordedAudioFile.getPath());
        }
    }

}
