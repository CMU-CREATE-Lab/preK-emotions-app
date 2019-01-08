package org.cmucreatelab.android.flutterprek.activities.student_section;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.GridView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.StudentSectionNavigationHandler;
import org.cmucreatelab.android.flutterprek.activities.adapters.EmotionIndexAdapter;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;

import java.util.ArrayList;
import java.util.List;

public class ChooseEmotionActivity extends StudentSectionActivityWithHeader {

    private final EmotionIndexAdapter.ClickListener listener = new EmotionIndexAdapter.ClickListener() {
        @Override
        public void onClick(Emotion emotion) {
            Log.d(Constants.LOG_TAG, "onClick emotion = " + emotion.getName());
            // track selection with GlobalHandler
            GlobalHandler.getInstance(getApplicationContext()).studentSectionNavigationHandler.emotionUuid = emotion.getUuid();
            // send to next activity
            Intent chooseCopingSkillActivity = new Intent(ChooseEmotionActivity.this, ChooseCopingSkillActivity.class);
            startActivity(chooseCopingSkillActivity);
        }
    };


    private LiveData<List<Emotion>> getLiveDataFromQuery(String classroomUuid, String studentUuid) {
        ArrayList<String> uuids = new ArrayList<>();
        if (!classroomUuid.isEmpty()) uuids.add(classroomUuid);
        if (!studentUuid.isEmpty()) uuids.add(studentUuid);
        return AppDatabase.getInstance(this).emotionDAO().getEmotionsOwnedBy(uuids);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StudentSectionNavigationHandler navigationHandler = GlobalHandler.getInstance(this).studentSectionNavigationHandler;
        LiveData<List<Emotion>> liveData = getLiveDataFromQuery(navigationHandler.classroomUuid, navigationHandler.studentUuid);
        liveData.observe(this, new Observer<List<Emotion>>() {
            @Override
            public void onChanged(@Nullable List<Emotion> emotions) {
                GridView emotionsGridView = findViewById(R.id.emotionsGridView);
                emotionsGridView.setAdapter(new EmotionIndexAdapter(ChooseEmotionActivity.this, emotions, listener));
            }
        });
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._student_section__activity_choose_emotion;
    }

}
