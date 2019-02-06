package org.cmucreatelab.android.flutterprek.activities.student_section;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.audio.AudioPlayer;
import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.CopingSkillMapper;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.StudentSectionNavigationHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.adapters.CopingSkillIndexAdapter;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;

import java.util.List;

public class ChooseCopingSkillActivity extends StudentSectionActivityWithTimeout {

    public static final String INTENT_MESSAGE = "message";
    public static final String INTENT_AUDIO_FILE = "audio_message";
    public static final String INTENT_BACKGROUND_COLOR = "background_color";

    private String message, backgroundColor, audioFile;

    private final CopingSkillIndexAdapter.ClickListener listener = new CopingSkillIndexAdapter.ClickListener() {
        @Override
        public void onClick(CopingSkill copingSkill) {
            Log.d(Constants.LOG_TAG, "onClick coping skill = " + copingSkill.getName());
            // track selection with GlobalHandler
            GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.copingSkillUuid = copingSkill.getUuid();
            Intent copingSkillActivity = CopingSkillMapper.createIntentFromCopingSkill(ChooseCopingSkillActivity.this, copingSkill);
            startActivity(copingSkillActivity);
        }
    };


    private LiveData<List<CopingSkill>> getLiveDataFromQuery(String classroomUuid, String studentUuid, String emotionUuid) {
        if (emotionUuid.isEmpty()) {
            return AppDatabase.getInstance(this).copingSkillDAO().getAllCopingSkills();
        }
        // TODO determine coping skills to display based on current class/student
        return AppDatabase.getInstance(this).copingSkillDAO().getCopingSkillsForEmotion(emotionUuid);
    }


    private LiveData<DbFile> getLiveDataFromQuery(String dbFileUuid) {
        return AppDatabase.getInstance(this).dbFileDAO().getDbFile(dbFileUuid);
    }


    private void parseIntent(Intent intent) {
        try {
            message = intent.getStringExtra(INTENT_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            backgroundColor = intent.getStringExtra(INTENT_BACKGROUND_COLOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            audioFile = intent.getStringExtra(INTENT_AUDIO_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void customizeDisplayForEmotion() {
        ((TextView)findViewById(R.id.textTitle)).setText(message != null ? message : "");
        findViewById(R.id.activityBackground).setBackgroundColor(Color.parseColor(backgroundColor != null ? backgroundColor : "#ffffff"));
    }


    private void playAudioFile() {
        if (audioFile != null) {
            final AudioPlayer audioPlayer = AudioPlayer.getInstance(getApplicationContext());
            getLiveDataFromQuery(audioFile).observe(this, new Observer<DbFile>() {
                @Override
                public void onChanged(@Nullable DbFile dbFile) {
                    // TODO check type?
                    audioPlayer.addAudioFromAssets(dbFile.getFilePath());
                    audioPlayer.playAudio();
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parseIntent(getIntent());
        customizeDisplayForEmotion();

        StudentSectionNavigationHandler navigationHandler = GlobalHandler.getInstance(this).studentSectionNavigationHandler;
        LiveData<List<CopingSkill>> liveData = getLiveDataFromQuery(navigationHandler.classroomUuid, navigationHandler.studentUuid, navigationHandler.emotionUuid);
        liveData.observe(this, new Observer<List<CopingSkill>>() {
            @Override
            public void onChanged(@Nullable List<CopingSkill> copingSkills) {
                GridView copingSkillsGridView = findViewById(R.id.copingSkillsGridView);
                copingSkillsGridView.setAdapter(new CopingSkillIndexAdapter(ChooseCopingSkillActivity.this, copingSkills, listener));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        playAudioFile();
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._student_section__activity_choose_coping_skill;
    }

}
