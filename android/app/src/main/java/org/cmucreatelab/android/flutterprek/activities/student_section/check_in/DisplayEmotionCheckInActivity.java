package org.cmucreatelab.android.flutterprek.activities.student_section.check_in;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.student_section.StudentSectionActivityWithTimeout;
import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;

import static org.cmucreatelab.android.flutterprek.activities.student_section.ChooseCopingSkillActivity.INTENT_BACKGROUND_COLOR;

public class DisplayEmotionCheckInActivity extends StudentSectionActivityWithTimeout {

    public static final String INTENT_CHECKIN_MESSAGE = "checkin_message";
    public static final String INTENT_CHECKIN_AUDIO_FILE = "checkin_audio_message";

    private String message, backgroundColor, audioFile;


    private LiveData<DbFile> getLiveDataFromQuery(String dbFileUuid) {
        return AppDatabase.getInstance(this).dbFileDAO().getDbFile(dbFileUuid);
    }


    private void parseIntent(Intent intent) {
        try {
            message = intent.getStringExtra(INTENT_CHECKIN_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            backgroundColor = intent.getStringExtra(INTENT_BACKGROUND_COLOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            audioFile = intent.getStringExtra(INTENT_CHECKIN_AUDIO_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void customizeDisplayForEmotion() {
        ((TextView)findViewById(R.id.textTitle)).setText(message != null ? message : "");
        findViewById(R.id.activityBackground).setBackgroundColor(Color.parseColor(backgroundColor != null ? backgroundColor : "#ffffff"));
    }


    private void playAudioFileForDisplayedEmotion() {
        if (audioFile != null) {
            final AudioPlayer audioPlayer = AudioPlayer.getInstance(getApplicationContext());
            getLiveDataFromQuery(audioFile).observe(this, new Observer<DbFile>() {
                @Override
                public void onChanged(@Nullable DbFile dbFile) {
                    // TODO check type?
                    playAudio(dbFile.getFilePath());
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent(getIntent());
        customizeDisplayForEmotion();

        findViewById(R.id.imagePlayAudioView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudioFileForDisplayedEmotion();
            }
        });
        findViewById(R.id.imageViewCheckmark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        playAudioFileForDisplayedEmotion();
    }


    @Override
    public void finish() {
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
        Intent intent = globalHandler.getSessionTracker().getNextIntentFromItinerary(this, 0);
        startActivity(intent);
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._check_in__activity_display_emotion;
    }

}
